package com.uporto.terminalapplication.network

import com.uporto.terminalapplication.model.Product
import java.util.UUID

data class CheckoutRequest (
    val idProductList: ArrayList<UUID>,
    val productQuantityList: ArrayList<Int>,
    val userId: UUID,
    val useAccumulatedDiscount: Float,
    val voucherId: Int
)