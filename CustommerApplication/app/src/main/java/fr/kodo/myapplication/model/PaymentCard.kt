package fr.kodo.myapplication.model

data class PaymentCard(
    val id: Int,
    val card_number: String,
    val card_holder_name: String,
    val expiration_month: Int,
    val expiration_year: Int,
    val cvv_code: String
)
