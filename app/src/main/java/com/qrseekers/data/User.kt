package com.qrseekers.data

data class User (
    var id: String = "",
    val username: String = "",
    val email: String = "None",
    val zone: String = "None",
    //val points: Int = 0, // todo: points
){
    override fun toString(): String {
        return "User(id='$id', username='$username', email='$email')"
    }
}