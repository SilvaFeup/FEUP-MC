package fr.kodo.myapplication.model
import java.util.*

data class Transaction(
    val id: Int,
    val customerId: Int,
    val totalAmount: Double,
    val voucherId: Int,
    val dateOrder: Date,
    val productList: ArrayList<Product>
)

