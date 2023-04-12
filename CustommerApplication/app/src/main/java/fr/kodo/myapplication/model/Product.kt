package fr.kodo.myapplication.model

import java.util.UUID

data class Product(
    val id: UUID,
    val name: String,
    val price: Double,
    val QRCodeID: Int,
    var quantity: Int = 1
)