package com.qrseekers.data

data class User (
    var id: String = "",
    val nickname: String = "",
    val email: String = "None",
    val zone: String = "None",
    val points: Int = 0,
    val gameName: String = "None",
    val profileImageBase64: String? = null,
){
    override fun toString(): String {
        return "User(id='$id', username='$nickname', email='$email', zone='$zone', points='$points', gameName='$gameName')"
    }
}