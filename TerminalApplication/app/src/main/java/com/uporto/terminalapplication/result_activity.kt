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
    private val tv_thanks by lazy { findViewById<TextView>(R.id.result_tv_thanks) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        if(intent.getStringExtra("valid")=="true"){
            tv_result.setText(R.string.positive_result_text)
            tv_gate.setText(R.string.open_gate)
        }
        else{
            tv_result.setText(R.string.negative_result_text)
            tv_gate.setText(R.string.closed_gate)
            tv_thanks.text = null
        }

        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val mainIntent = Intent(this@result_activity,MainActivity::class.java)
                startActivity(mainIntent)
            }
        })
    }
}
