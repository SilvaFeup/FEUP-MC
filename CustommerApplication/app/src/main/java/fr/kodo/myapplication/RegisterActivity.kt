package fr.kodo.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import fr.kodo.myapplication.controller.RegisterController
import java.security.MessageDigest

class RegisterActivity : AppCompatActivity() {
    val RegisterController = RegisterController()

    val edtName by lazy { findViewById<EditText>(R.id.register_edt_name) }
    val edtNickname by lazy { findViewById<EditText>(R.id.register_edt_nickname) }
    val edtPassword by lazy { findViewById<EditText>(R.id.register_edt_password) }
    val edtConfirmPassword by lazy { findViewById<EditText>(R.id.register_edt_confirm_password) }

    val edtCardNumber by lazy { findViewById<EditText>(R.id.register_edt_card_number) }
    val edtCardHolderName by lazy { findViewById<EditText>(R.id.register_edt_card_holder_name) }
    val edtCardExpirationDate by lazy { findViewById<EditText>(R.id.register_edt_card_expiration_date) }
    val edtCardSecurityCode by lazy { findViewById<EditText>(R.id.register_edt_card_security_code) }

    val btRegister by lazy { findViewById<Button>(R.id.register_bt_register) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btRegister.setOnClickListener{
            val md = MessageDigest.getInstance("SHA-256")

            //TODO: Change the hash algorithm

            val name = edtName.text.toString()
            val nickname = edtNickname.text.toString()
            val password = md.digest(edtPassword.text.toString().encodeToByteArray()).toString()
            val confirmPassword = md.digest(edtConfirmPassword.text.toString().encodeToByteArray()).toString()
            val cardNumber = edtCardNumber.text.toString()
            val cardHolderName = edtCardHolderName.text.toString()
            val cardExpirationDate = edtCardExpirationDate.text.toString()
            val cardCvvCode = edtCardSecurityCode.text.toString()

            println(password)
            println(confirmPassword)

            if (name.isEmpty() || nickname.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || cardNumber.isEmpty() || cardHolderName.isEmpty() || cardExpirationDate.isEmpty() || cardCvvCode.isEmpty()) {
                return@setOnClickListener
            }
            else {
                RegisterController.register(name, nickname, password, confirmPassword, cardNumber, cardHolderName, cardExpirationDate, cardCvvCode)
            }
        }
    }
}