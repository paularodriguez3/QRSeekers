package com.qrseekers.data

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreInstance {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
}