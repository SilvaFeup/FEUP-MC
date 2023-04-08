package fr.kodo.myapplication.model

data class Product(
    val id: Int, val name: String, val price: Double, val QRCodeID: Int, var quantity: Int = 1
)