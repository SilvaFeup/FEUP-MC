package fr.kodo.myapplication.controller

import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import fr.kodo.myapplication.R
import java.util.*

private const val SIZE = 600
private const val ISO_SET = "ISO-8859-1"

fun Generate_QR_Code(message: String): Bitmap?{

    val result: BitMatrix
    val hints = Hashtable<EncodeHintType, String>().apply { put(EncodeHintType.CHARACTER_SET, ISO_SET) }
    val width = SIZE
    val format: BarcodeFormat = BarcodeFormat.QR_CODE

    try {
        result = MultiFormatWriter().encode(message, format, width, SIZE, hints)
    }
    catch (exception: Exception) {
        Log.println(Log.ERROR, "QRCode", exception.stackTraceToString())
        return null
    }

    val w = result.width
    val h = result.height
    val colorDark = R.color.black
    val colorLight = R.color.white
    val pixels = IntArray(w * h)
    for (line in 0 until h) {
        val offset = line * w
        for (col in 0 until w)
            pixels[offset + col] = if (result.get(col, line)) colorDark else colorLight
    }
    return Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).apply { setPixels(pixels, 0, w, 0, 0, w, h) }
}