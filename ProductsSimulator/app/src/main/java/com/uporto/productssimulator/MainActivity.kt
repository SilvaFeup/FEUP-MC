package com.uporto.productssimulator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    val et_name by lazy { findViewById<EditText>(R.id.et_name) }
    val et_price by lazy { findViewById<EditText>(R.id.et_price) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.bt_generate).setOnClickListener{vw -> onButtonClick(vw)}
    }

    private fun onButtonClick(vw: View){
        val intent = Intent(this, QRCodeActivity::class.java)
        val message = et_name.text.toString() +","+ et_price.text.toString()
        intent.putExtra("message",message)
        intent.putExtra("name",et_name.text.toString())
        intent.putExtra("price",et_price.text.toString())
        startActivity(intent)
    }
}