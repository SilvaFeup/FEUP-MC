package com.uporto.terminalapplication.model

import java.util.*

data class Product(
    val id: UUID,
    val name: String,
    val price: Double,
    val QRCodeID: Int,
    var quantity: Int = 1
)