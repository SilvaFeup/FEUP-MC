package fr.kodo.myapplication.controller

import fr.kodo.myapplication.model.PaymentCard
import fr.kodo.myapplication.model.User
import java.util.*

class RegisterController {
    fun register(
        name: String,
        nickname: String,
        password: String,
        confirmPassword: String,
        card_number: String,
        card_holder_name: String,
        expiration_date: String,
        security_code: String
    ){
        val user: User = User(UUID.randomUUID(), nickname, password, name, "", PaymentCard(0, card_number, card_holder_name, 0, 0, security_code))
    }
}