package fr.kodo.myapplication

import KeyStoreUtils
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.kodo.myapplication.controller.AuthController
import java.net.PasswordAuthentication
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterActivity : AppCompatActivity() {
    val RegisterController = AuthController()

    val edtName by lazy { findViewById<EditText>(R.id.register_edt_name) }
    val edtNickname by lazy { findViewById<EditText>(R.id.register_edt_nickname) }
    val edtPassword by lazy { findViewById<EditText>(R.id.register_edt_password) }
    val edtConfirmPassword by lazy { findViewById<EditText>(R.id.register_edt_confirm_password) }

    val edtCardNumber by lazy { findViewById<EditText>(R.id.register_edt_card_number) }
    val edtCardHolderName by lazy { findViewById<EditText>(R.id.register_edt_card_holder_name) }
    val edtCardExpirationMonth by lazy { findViewById<EditText>(R.id.register_edt_card_expiration_month) }
    val edtCardExpirationYear by lazy { findViewById<EditText>(R.id.register_edt_card_expiration_year) }
    val edtCardSecurityCode by lazy { findViewById<EditText>(R.id.register_edt_card_security_code) }

    val progressBar by lazy { findViewById<ProgressBar>(R.id.register_progress_bar) }

    val btRegister by lazy { findViewById<Button>(R.id.register_bt_register) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btRegister.setOnClickListener{
            val name = edtName.text.toString()
            val nickname = edtNickname.text.toString()
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirmPassword.text.toString()
            val cardNumber = edtCardNumber.text.toString()
            val cardHolderName = edtCardHolderName.text.toString()
            val cardExpirationMonth = edtCardExpirationMonth.text.toString()
            val cardExpirationYear = edtCardExpirationYear.text.toString()
            val cardCvvCode = edtCardSecurityCode.text.toString()

            var responseCode = -1

            progressBar.visibility = ProgressBar.VISIBLE
            btRegister.isEnabled = false

            lifecycleScope.launch {
                try {
                    responseCode = RegisterController.register(name,nickname, password, confirmPassword,
                        cardNumber, cardHolderName, cardExpirationMonth,cardExpirationYear, cardCvvCode)

                }catch (e: Exception) {
                    Log.e("Erro: ", e.stackTraceToString())
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
                }

                withContext(Dispatchers.Main){
                    if(responseCode == 1){
                        //Switch to LoginActivity
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }

                    progressBar.visibility = ProgressBar.GONE
                    btRegister.isEnabled = true
                }
            }
        }
    }
}