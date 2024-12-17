package com.qrseekers.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Estado de autenticación (LiveData)
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        forceLogoutOnStart()
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
            "team" to "None"
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
}

// Definición del estado de autenticación
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
