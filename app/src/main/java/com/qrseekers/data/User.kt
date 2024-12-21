package com.qrseekers.data

data class User (
    var id: String = "",
    val nickname: String = "",
    val email: String = "None",
    val zone: String = "None",
    val points: Int = 0,
    val gameName: String = "None"
){
    override fun toString(): String {
        return "User(id='$id', username='$nickname', email='$email', zone='$zone', points='$points', gameName='$gameName')"
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "username" to nickname,
            "email" to email,
            "zone" to zone,
            "points" to points,
            "gameName" to gameName
        )
    }
}