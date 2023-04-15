package fr.kodo.myapplication.model
import java.util.*

data class Transaction(
    val id: Int,
    val customer_id: Int,
    val total_amount: Double,
    val voucher_id: Int,
    val date_order: Date
)

