package fr.kodo.myapplication.controller

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat.startActivityForResult

private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"

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

fun scan(Activity: Activity){
    try {
        val intent = Intent(ACTION_SCAN).apply {
            putExtra("SCAN_MOD", "QR_CODE_MODE")}
        startActivityForResult(Activity, intent, 0, null)
    }
    catch (e: ActivityNotFoundException){
        showDialog(Activity, "No Scanner Found", "Download a scanner code app?", "Yes", "No").show()
    }
}
