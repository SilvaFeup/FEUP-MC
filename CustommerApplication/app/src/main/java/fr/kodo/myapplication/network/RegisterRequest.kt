package fr.kodo.myapplication.network


data class RegisterRequest(
    val username: String,
    val password: String,
    val name: String,
    val card_number: String,
    val card_holder_name: String,
    val expiration_month : Int,
    val expiration_year: Int,
    val cvv_code: String,
    val public_key: String
)