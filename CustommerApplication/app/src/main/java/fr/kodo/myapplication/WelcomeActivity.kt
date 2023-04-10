package fr.kodo.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.controller.ShoppingBasketAdapter
import fr.kodo.myapplication.model.Product
import fr.kodo.myapplication.controller.scan

private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"
class WelcomeActivity : AppCompatActivity() {

    var shoppingBasket = ArrayList<Product>()
    val shoppingBasketView by lazy { findViewById<RecyclerView>(R.id.welcome_rv_shopping_basket) }
    val btAddProduct by lazy { findViewById<Button>(R.id.welcome_bt_add_product) }
    val btCheckout by lazy { findViewById<Button>(R.id.welcome_bt_checkout) }
    val btLogout by lazy { findViewById<Button>(R.id.welcome_bt_logout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)

        shoppingBasketView.adapter = ShoppingBasketAdapter(shoppingBasket)
        shoppingBasketView.layoutManager = LinearLayoutManager(this)

        btAddProduct.setOnClickListener { scan(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (resultCode == AppCompatActivity.RESULT_OK){
                var product =data?.getStringExtra("SCAN_RESULT")?:""
                if (product != "") {

                    var result = product.split(",")

                    shoppingBasket.add(
                        Product(
                            shoppingBasket.size,
                            result[0].toString(),
                            result[1].toDouble(),
                            1
                        )
                    )
                    shoppingBasketView.adapter?.notifyItemInserted(shoppingBasket.size - 1)
                }
            }
        }
    }

}

