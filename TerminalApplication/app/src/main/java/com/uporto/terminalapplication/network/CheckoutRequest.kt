package com.uporto.terminalapplication.network

import com.uporto.terminalapplication.model.Product
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

data class CheckoutRequest (
    val idProductList: ArrayList<UUID>,
    val productQuantityList: ArrayList<Int>,
    val userId: UUID,
    val useAccumulatedDiscount: Float,
    val voucherId: Int,
    val date: String
)