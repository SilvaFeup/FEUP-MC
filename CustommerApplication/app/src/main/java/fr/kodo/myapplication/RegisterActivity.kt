package fr.kodo.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.kodo.myapplication.controller.AuthController
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers


import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterActivity : AppCompatActivity() {
    private val registerController = AuthController()

    private val edtName by lazy { findViewById<EditText>(R.id.register_edt_name) }
    private val edtNickname by lazy { findViewById<EditText>(R.id.register_edt_nickname) }
    private val edtPassword by lazy { findViewById<EditText>(R.id.register_edt_password) }
    private val edtConfirmPassword by lazy { findViewById<EditText>(R.id.register_edt_confirm_password) }

    private val edtCardNumber by lazy { findViewById<EditText>(R.id.register_edt_card_number) }
    private val edtCardHolderName by lazy { findViewById<EditText>(R.id.register_edt_card_holder_name) }
    private val edtCardExpirationMonth by lazy { findViewById<EditText>(R.id.register_edt_card_expiration_month) }
    private val edtCardExpirationYear by lazy { findViewById<EditText>(R.id.register_edt_card_expiration_year) }
    private val edtCardSecurityCode by lazy { findViewById<EditText>(R.id.register_edt_card_security_code) }

    private val progressBar by lazy { findViewById<ProgressBar>(R.id.register_progress_bar) }

    private val btRegister by lazy { findViewById<Button>(R.id.register_bt_register) }
    private val btLogin by lazy { findViewById<Button>(R.id.register_bt_login) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btLogin.setOnClickListener{
            finish()
        }

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


            progressBar.visibility = ProgressBar.VISIBLE
            btRegister.isEnabled = false

            lifecycleScope.launch {
                try {
                    registerController.register(name,nickname, password, confirmPassword,
                        cardNumber, cardHolderName, cardExpirationMonth,cardExpirationYear, cardCvvCode)

                }catch (e: Exception) {
                    Log.e("RegisterActivity: ", e.stackTraceToString())
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
                    progressBar.visibility = ProgressBar.GONE
                    btRegister.isEnabled = true
                    return@launch
                }

                withContext(Dispatchers.Main){
                    //Switch to LoginActivity
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)

                    progressBar.visibility = ProgressBar.GONE
                    btRegister.isEnabled = true
                }
            }
        }
    }
}