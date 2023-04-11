package fr.kodo.myapplication.network


data class LoginResponse(
    val message: String,
    val userId: String,
    val supermarket_publickey: String
)
