package com.qrseekers.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.data.Game
import com.qrseekers.data.User

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Using mutableStateOf to hold the list of games
    private val _user = mutableStateOf<User>(User())
    val user: State<User> get() = _user

    // Estado de autenticación (LiveData)
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        //todo: decide if force logout Anna does not think it is a good idea:)
        //forceLogoutOnStart()
    }

    // Cerrar sesión al iniciar la app
    private fun forceLogoutOnStart() {
        auth.signOut()
       _authState.value = AuthState.Unauthenticated
    }

    // Registro de un nuevo usuario
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
                        saveUserToFirestore(userId, email, nickname)
                    } else {
                        _authState.value = AuthState.Error("User ID not found.")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Registration failed")
                }
            }
    }

    // Guardar datos del usuario en Firestore
    private fun saveUserToFirestore(userId: String, email: String, nickname: String) {
        val userMap = mapOf(
            "name" to nickname,
            "email" to email,
            "participates" to "None",
            "team" to "None",
            "zone" to "None"
        )

        firestore.collection("users").document(userId)
            .set(userMap)
            .addOnSuccessListener {
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error("Failed to save user: ${exception.message}")
            }
    }

    // Inicio de sesión de un usuario existente
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
    }

    // Cerrar sesión
    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    // Fetch user data from Firestore after login
    private fun fetchUserFromFirestore(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val username = document.getString("name") ?: "Unknown"
                    val email = document.getString("email") ?: "Unknown"
                    val zone = document.getString("zone") ?: "None"
                    val participates = document.getString("participates") ?: "None"
                    val team = document.getString("team") ?: "None"


                    // Update the user data in the ViewModel
                    _user.value = User(id = userId, username = username, email = email, zone = zone)

                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("User data not found.")
                }
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error("Failed to fetch user: ${exception.message}")
            }
    }

    fun setUserGameParticipation(userId: String, gameId: String) {
        val userMap = mapOf(
            "participates" to gameId  // Set the participates field to the game ID
        )

        firestore.collection("users").document(userId)
            .update(userMap)
            .addOnSuccessListener {
                Log.d("UserGame", "Successfully updated game participation for user $userId")
            }
            .addOnFailureListener { exception ->
                Log.e("UserGame", "Failed to update game participation: ${exception.message}")
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
