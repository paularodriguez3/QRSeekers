package com.qrseekers.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.data.User


class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Using mutableStateOf to hold the user data
    private val _user = mutableStateOf<User>(User())
    val user: State<User> get() = _user

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    var gameName = ""

    init {
        // Handle force logout if needed
    }

    // Sign up a new user
    fun signup(email: String, password: String, nickname: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // Create the User object with the provided parameters
                        val user = User(
                            id = userId,
                            nickname = nickname,
                            email = email,
                            zone = "None",  // Default value
                            points = 0,     // Default points
                            gameName = "None",  // Default value
                            profileImageBase64 = null,
                        )

                        saveUserToFirestore(user)
                    } else {
                        _authState.value = AuthState.Error("User ID not found.")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Registration failed")
                }
            }
    }

    // Save the user to Firestore
    fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.id)
            .set(user)
            .addOnSuccessListener {
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error("Failed to save user: ${exception.message}")
            }
    }

    // Login an existing user
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }

        Log.d("AuthViewModel", "Login in")

    }

    // Sign out the current user
    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    // Fetch user data from Firestore after login
    fun fetchUserFromFirestore(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nickname = document.getString("nickname") ?: "Unknown"
                    val email = document.getString("email") ?: "Unknown"
                    val zone = document.getString("zone") ?: "None"

                    // Update the user data in the ViewModel
                    _user.value = User(id = userId, nickname = nickname, email = email, zone = zone)

                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("User data not found.")
                }
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error("Failed to fetch user: ${exception.message}")
            }
    }

    // Update user participation in a game
    fun setUserGameParticipation(userId: String, gameId: String, gameName: String) {
        val userDoc = FirebaseFirestore.getInstance().collection("users").document(userId)
        userDoc.update(
            mapOf(
                "gameId" to gameId,
                "gameName" to gameName // Actualiza el nombre del juego seleccionado
            )
        ).addOnSuccessListener {
            Log.d("AuthViewModel", "Game participation updated for user: $userId")
        }.addOnFailureListener { e ->
            Log.e("AuthViewModel", "Error updating game participation: ${e.localizedMessage}")
        }
    }


    fun addPoints(gamePoints: Int) {
        _user.value = _user.value.copy(points = _user.value.points + gamePoints)

    }

    // Check if the user is authenticated
    fun checkUserAuthentication(): Boolean {
        _authState.value = AuthState.Loading // Set state to loading during the check

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is authenticated, fetch additional user data from Firestore if needed
            fetchUserFromFirestore(currentUser.uid)
            return true
        } else {
            // User is not authenticated
            _authState.value = AuthState.Unauthenticated
            return false
        }
    }

    fun resetPoints() {
        _user.value = _user.value.copy(points = 0) // Reiniciar puntos en el estado local

        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId)
                .update("points", 0) // Reiniciar puntos en Firestore
                .addOnSuccessListener {
                    Log.d("AuthViewModel", "Points reset successfully in Firestore.")
                }
                .addOnFailureListener { exception ->
                    Log.e("AuthViewModel", "Failed to reset points in Firestore: ${exception.message}")
                }
        }
    }


}

// Definición del estado de autenticación
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}


