package fr.kodo.myapplication.controller

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat.startActivityForResult

private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"


fun scan(Activity: Activity){
    try {
        val intent = Intent(ACTION_SCAN).apply {
            putExtra("SCAN_MOD", "QR_CODE_MODE")}
        startActivityForResult(Activity, intent, 0, null)
    }
    catch (e: ActivityNotFoundException){
        val downloadDialog = AlertDialog.Builder(Activity)
        downloadDialog.setTitle("No Scanner Found")
        downloadDialog.setMessage("Download a scanner code app?")
        downloadDialog.setPositiveButton("Yes") { _, _ ->
            val uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            Activity.startActivity(intent)
        }
        downloadDialog.setNegativeButton("No", null)
        downloadDialog.create().show()
    }
}
