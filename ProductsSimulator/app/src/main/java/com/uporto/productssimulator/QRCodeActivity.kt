package com.uporto.productssimulator

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.security.KeyPairGeneratorSpec
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal
import kotlin.concurrent.thread

private const val SIZE = 600
private const val ISO_SET = "ISO-8859-1"
private var generated = false

data class PubKey(var modulus: ByteArray, var exponent: ByteArray)

class QRCodeActivity : AppCompatActivity() {
    private var content = ByteArray(0)
    private val tv_name by lazy { findViewById<TextView>(R.id.tv_name)}
    private val tv_price by lazy { findViewById<TextView>(R.id.tv_price)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        generateAndStoreKeys()
        generated = keysPresent()

        val image = findViewById<ImageView>(R.id.image_code)
        val message = intent.getStringExtra("message")
        if (message != null){
            content = message.toByteArray()

        }
        else{
            content = ByteArray(0)
        }

            //TODO crypt with keys on sever
        /*
        if(generated){
            encryptResult()
            tv_name.text = "Name : ${content.toString(Charsets.UTF_8)}"
        }*/
        tv_name.text = "Name : " + intent.getStringExtra("name")
        tv_price.text = "price : " + intent.getStringExtra("price") + "€"

        val backButton = findViewById<Button>(R.id.bt_back)
        backButton.setOnClickListener {
            val mainIntent = Intent(this@QRCodeActivity, MainActivity::class.java)
            startActivity(mainIntent)
        }

        thread {
            encodeAsBitmap(content.toString(Charsets.UTF_8)).also { runOnUiThread { image.setImageBitmap(it) } }
        }
    }
        /*
                QR_Code
         */
    private fun encodeAsBitmap(str : String): Bitmap?{
        val result: BitMatrix
        val hints = Hashtable<EncodeHintType, String>().apply { put(EncodeHintType.CHARACTER_SET, ISO_SET) }
        var width = SIZE
        val format: BarcodeFormat = BarcodeFormat.QR_CODE

        try {
            result = MultiFormatWriter().encode(str, format, width, SIZE, hints)
        }
        catch (e: Exception) {
            content = "\n${e.message}".toByteArray()
            runOnUiThread { tv_name.text = content.toString() }
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

    /*
            Crypto
     */
    fun byteArrayToHex(ba : ByteArray) :String{
        val stringBuilder = StringBuilder(ba.size * 2)
        for (bit in ba) stringBuilder.append(String.format("%02x", bit))
        return stringBuilder.toString()
    }

    private fun generateAndStoreKeys(): Boolean{
        try {
            if (!generated){
                val spec = KeyPairGeneratorSpec.Builder(this)
                    .setKeySize(Constants.KEY_SIZE)
                    .setAlias(Constants.keyname)
                    .setSubject(X500Principal("CN=" + Constants.keyname))
                    .setSerialNumber(BigInteger.valueOf(Constants.serialNr))
                    .setStartDate(GregorianCalendar().time)
                    .setEndDate(GregorianCalendar().apply { add(Calendar.YEAR, 10)}.time)
                    .build()

                KeyPairGenerator.getInstance(Constants.KEY_ALGO, Constants.ANDROID_KEYSTORE).run {
                    initialize(spec)
                    generateKeyPair()
                }
            }
        }
        catch (ex: Exception){
            print("error caught!")
            return false
        }
        return true
    }

    private fun keysPresent(): Boolean{
        val entry = KeyStore.getInstance(Constants.ANDROID_KEYSTORE).run {
            load(null)
            getEntry(Constants.keyname, null)
        }
        return (entry != null)
    }

    private fun getPubKey(): PubKey{
        val pKey = PubKey(ByteArray(0), ByteArray(0))
        try {
            val entry =KeyStore.getInstance(Constants.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(Constants.keyname,null)
            }
            val pub = (entry as KeyStore.PrivateKeyEntry).certificate.publicKey
            pKey.modulus = (pub as RSAPublicKey).modulus.toByteArray()
            pKey.exponent = (pub as RSAPublicKey).publicExponent.toByteArray()
        }
        catch (ex : Exception){
            print( "error caught!")
        }
        return pKey
    }

    private fun getPrivExp():ByteArray{
        var exp = ByteArray(0)
        try {
            val entry = KeyStore.getInstance(Constants.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(Constants.keyname, null)
            }
            val priv = (entry as KeyStore.PrivateKeyEntry).privateKey
            exp = (priv as RSAPrivateKey).privateExponent.toByteArray()
        }
        catch (ex: Exception) {
           print("error caught!")
        }
        return exp
    }

    private fun showKeys(){
        val pkey = getPubKey()
        val privExp = getPrivExp()

        print(String.format(InStrings.showKeysFormat, pkey.modulus.size, byteArrayToHex(pkey.modulus),
            byteArrayToHex(pkey.exponent), privExp.size, byteArrayToHex(privExp)))
    }

    private fun encryptResult(){
        if (content.isEmpty()) return
        var result = ByteArray(0)
        try {
            val entry = KeyStore.getInstance(Constants.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(Constants.keyname, null)
            }
            val prKey = (entry as KeyStore.PrivateKeyEntry).privateKey
            result = Cipher.getInstance(Constants.ENC_ALGO).run {
                init(Cipher.ENCRYPT_MODE, prKey)
                doFinal(content)
            }
            content = result
        }
        catch (e: Exception) {
            print(e.message)
        }

    }
}