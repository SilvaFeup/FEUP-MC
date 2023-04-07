package fr.kodo.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.kodo.myapplication.controller.RegisterController
import java.net.PasswordAuthentication
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    val RegisterController = RegisterController()

    val edtName by lazy { findViewById<EditText>(R.id.register_edt_name) }
    val edtNickname by lazy { findViewById<EditText>(R.id.register_edt_nickname) }
    val edtPassword by lazy { findViewById<EditText>(R.id.register_edt_password) }
    val edtConfirmPassword by lazy { findViewById<EditText>(R.id.register_edt_confirm_password) }

    val edtCardNumber by lazy { findViewById<EditText>(R.id.register_edt_card_number) }
    val edtCardHolderName by lazy { findViewById<EditText>(R.id.register_edt_card_holder_name) }
    val edtCardExpirationMonth by lazy { findViewById<EditText>(R.id.register_edt_card_expiration_month) }
    val edtCardExpirationYear by lazy { findViewById<EditText>(R.id.register_edt_card_expiration_year) }
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
            val cardExpirationMonth = edtCardExpirationMonth.text.toString().toInt();
            val cardExpirationYear = edtCardExpirationYear.text.toString().toInt();
            val cardCvvCode = edtCardSecurityCode.text.toString()

            var responseCode = -1

            lifecycleScope.launch {
                try {
                    try {
                        responseCode = RegisterController.register(name, password, confirmPassword, cardNumber, cardHolderName, cardExpirationMonth,cardExpirationYear, cardCvvCode)
                    }catch (e: Exception) {
                        Log.e("Erro: ", e.toString())
                    }
                } catch (e: Exception) {
                    // Handle network or server error
                }
            }



            Toast.makeText(this, "Registered : " + responseCode, Toast.LENGTH_SHORT).show()
        }
    }
}