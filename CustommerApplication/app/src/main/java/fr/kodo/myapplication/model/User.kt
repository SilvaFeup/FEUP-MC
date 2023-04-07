package fr.kodo.myapplication.model

import java.util.UUID

data class User(
    val uuid: UUID,
    val username: String,
    val password: String,
    val name: String,
    val public_key: String,
    val payment_card: PaymentCard
)
