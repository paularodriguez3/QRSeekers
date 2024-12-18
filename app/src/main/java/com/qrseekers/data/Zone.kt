package com.qrseekers.data

data class Zone (
    var id: String = "None",
    val name: String = "None", // name of the zone
    val hint: String? = "", //hint for finding the qr code
    val questions: List<String>? = null, // ids of questions in a zone
)