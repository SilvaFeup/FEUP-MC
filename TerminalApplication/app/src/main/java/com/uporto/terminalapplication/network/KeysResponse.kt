package com.uporto.terminalapplication.network

import java.security.PublicKey

data class KeysResponse(
    val message :String,
    val pubKey : String
)