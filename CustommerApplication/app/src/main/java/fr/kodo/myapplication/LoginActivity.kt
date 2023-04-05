package fr.kodo.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    val edtNickname by lazy { findViewById<EditText>(R.id.login_edt_nickname) }
    val edtPassword by lazy { findViewById<EditText>(R.id.login_edt_password) }
    val btnLogin by lazy { findViewById<Button>(R.id.login_bt_login) }
    val btnRegister by lazy { findViewById<Button>(R.id.login_bt_register) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //supportActionBar?.hide()

        btnLogin.setOnClickListener {
            val nickname = edtNickname.text.toString()
            val password = edtPassword.text.toString()
            if (nickname.isEmpty() || password.isEmpty()) {
                return@setOnClickListener
            }
            else {
                login(nickname, password)
            }
        }

        btnRegister.setOnClickListener {
            //Switch to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(nickname: String, password: String) {
        Toast.makeText(this, "Login$nickname:$password", Toast.LENGTH_SHORT).show()
    }


}