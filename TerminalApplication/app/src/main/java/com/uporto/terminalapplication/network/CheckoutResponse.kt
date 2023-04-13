package com.uporto.terminalapplication.network

data class CheckoutResponse (
    val message: String,
    val total: Float,
    val discount: Float
)