package fr.kodo.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.kodo.myapplication.controller.RegisterController
import java.net.PasswordAuthentication
import java.security.MessageDigest
import java.security.SecureRandom

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
            val name = edtName.text.toString()
            val nickname = edtNickname.text.toString()
            val password = PasswordAuthentication(nickname, edtPassword.text.toString().toCharArray())
            val confirmPassword = PasswordAuthentication(nickname, edtConfirmPassword.text.toString().toCharArray())
            val cardNumber = edtCardNumber.text.toString()
            val cardHolderName = edtCardHolderName.text.toString()
            val cardExpirationDate = edtCardExpirationDate.text.toString()
            val cardCvvCode = edtCardSecurityCode.text.toString()

            var responseCode = -1
            try {
                responseCode = RegisterController.register(name, password, confirmPassword, cardNumber, cardHolderName, cardExpirationDate, cardCvvCode)
            }catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                println(e.message)
            }

            Toast.makeText(this, "Registered : " + responseCode, Toast.LENGTH_SHORT).show()
        }
    }
}