package fr.kodo.myapplication.network


data class LoginResponse(
    val error: Int,
    val message: String,
    val userId: String,
    val supermarket_publickey: String
)
