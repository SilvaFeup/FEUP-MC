package com.uporto.terminalapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"

class MainActivity : AppCompatActivity() {
    private val button by lazy{ findViewById<Button>(R.id.main_bt_checkout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener{scan()}
    }

    // If we have not the scan app on the device
    private fun showDialog(act: Activity, title: CharSequence, message: CharSequence, buttonYes: CharSequence, buttonNo: CharSequence): AlertDialog {
        val downloadDialog = AlertDialog.Builder(act)
        downloadDialog.setTitle(title)
        downloadDialog.setMessage(message)
        downloadDialog.setPositiveButton(buttonYes) { _, _ ->
            val uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            act.startActivity(intent)
        }
        downloadDialog.setNegativeButton(buttonNo, null)
        return downloadDialog.create()
    }

    private fun scan(){
        try {
            val intent = Intent(ACTION_SCAN).apply {
                putExtra("SCAN_MOD", "QR_CODE_MODE")}
            startActivityForResult(intent,0)
        }
        catch (e: ActivityNotFoundException){
            showDialog(this, "No Scanner Found", "Download a scanner code app?", "Yes", "No").show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                val resultIntent = Intent(this, result_activity::class.java)
                resultIntent.putExtra("result",data?.getStringExtra("SCAN_RESULT")?:"")
                startActivity(resultIntent)
            }
        }
    }
}