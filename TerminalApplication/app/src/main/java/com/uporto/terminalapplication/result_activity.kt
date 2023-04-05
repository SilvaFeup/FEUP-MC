package com.uporto.terminalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class result_activity : AppCompatActivity() {
    private val button by lazy { findViewById<Button>(R.id.result_bt_back)}
    private val tv_result by lazy { findViewById<TextView>(R.id.result_tv_outcome) }
    private val tv_gate by lazy { findViewById<TextView>(R.id.result_tv_doorMessage) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)



        //TODO set text of both messages with the list of products
        tv_result.text = intent.getStringExtra("result")


        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val mainIntent = Intent(this@result_activity,MainActivity::class.java)
                startActivity(mainIntent)
            }
        })
    }
}