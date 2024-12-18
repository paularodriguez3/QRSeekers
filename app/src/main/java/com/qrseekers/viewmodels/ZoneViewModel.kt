package com.qrseekers.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.qrseekers.data.Game
import com.qrseekers.data.Zone
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ZoneViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // Using mutableStateOf to hold the list of zones
    private val _zones = mutableStateOf<List<Zone>>(emptyList())
    val zones: State<List<Zone>> get() = _zones

    // Current zone for the user
    private val _currentZone = mutableStateOf<Zone?>(null)
    val currentZone: State<Zone?> get() = _currentZone

    private val logTag = "ZoneViewModel"

    // Function to load zones from Firestore
    fun loadZones() {
        viewModelScope.launch {
            try {
                // Fetch the collection of zones from Firestore
                val zonesSnapshot = firestore.collection("Zones").get().await()

                // Map the documents to Zone objects
                val zonesList = zonesSnapshot.documents.mapNotNull { document ->
                    document.toObject(Zone::class.java)?.apply {
                        id = document.id
                    }
                }

                // Update the state with the fetched zones
                _zones.value = zonesList

                Log.d(logTag, "Loaded zones: $zonesList")

            } catch (e: Exception) {
                // Handle error (e.g., logging or showing an error message)
                Log.e(logTag, "Error loading zones: ${e.localizedMessage}")
            }
        }
    }

    // Function to set the current game for the user
    fun setCurrentZone(zoneId: String) {
        if (zoneId == "None"){
            Log.d(logTag, "Current zone set to: null")

            clearCurrentZone()
            return
        }
        val zone = _zones.value.find { it.id == zoneId }
        if (zone != null) {
            _currentZone.value = zone
            Log.d(logTag, "Current zone set to: $zone")
        } else {
            Log.e(logTag, "zone with ID a{$zoneId}a not found")
            // Load the zone from Firebase if it's not found locally
            loadZoneFromFirebase(zoneId)
        }
    }

    private fun loadZoneFromFirebase(zoneId: String) {

        // Firebase loading logic goes here
        val db = FirebaseFirestore.getInstance()

        db.collection("Zones")
            .document(zoneId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val zone = document.toObject(Zone::class.java)?.apply{
                        id = document.id
                    }
                    zone?.let {
                        _currentZone.value = it
                        Log.d(logTag, "Current zone loaded from Firebase: $it")
                    }
                } else {
                    Log.e(logTag, "No zone found with ID $zoneId in Firebase")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(logTag, "Error loading zone from Firebase: $exception")
            }
    }

    // Optionally, you can clear the current game when the user logs out or exits the game
    fun clearCurrentZone() {
        _currentZone.value = null
        Log.d(logTag, "Current game cleared")
    }
}
