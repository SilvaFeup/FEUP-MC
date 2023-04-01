package com.uporto.productssimulator

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.util.*
import kotlin.concurrent.thread

private const val SIZE = 600
private const val ISO_SET = "ISO-8859-1"

class QRCodeActivity : AppCompatActivity() {
    private var content = ""
    private val tv_name by lazy { findViewById<TextView>(R.id.tv_name)}
    private val tv_price by lazy { findViewById<TextView>(R.id.tv_price)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        val image = findViewById<ImageView>(R.id.image_code)

        content = intent.getStringExtra("message").toString()
        tv_name.text = "Name : " + intent.getStringExtra("name")
        tv_price.text = "price : " + intent.getStringExtra("price") + "€"

        val backButton = findViewById<Button>(R.id.bt_back)
        backButton.setOnClickListener {
            val mainIntent = Intent(this@QRCodeActivity, MainActivity::class.java)
            startActivity(mainIntent)
        }

        thread {
            encodeAsBitmap(content).also { runOnUiThread { image.setImageBitmap(it) } }
        }
    }

    private fun encodeAsBitmap(str : String): Bitmap?{
        val result: BitMatrix
        val hints = Hashtable<EncodeHintType, String>().apply { put(EncodeHintType.CHARACTER_SET, ISO_SET) }
        var width = SIZE
        val format: BarcodeFormat = BarcodeFormat.QR_CODE

        try {
            result = MultiFormatWriter().encode(str, format, width, SIZE, hints)
        }
        catch (e: Exception) {
            content += "\n${e.message}"
            runOnUiThread { tv_name.text = content }
            return null
        }
        val w = result.width
        val h = result.height
        val colorDark = resources.getColor(R.color.black, null)
        val colorLight = resources.getColor(R.color.white, null)
        val pixels = IntArray(w * h)
        for (line in 0 until h) {
            val offset = line * w
            for (col in 0 until w)
                pixels[offset + col] = if (result.get(col, line)) colorDark else colorLight
        }
        return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).apply { setPixels(pixels, 0, w, 0, 0, w, h) }
    }
}