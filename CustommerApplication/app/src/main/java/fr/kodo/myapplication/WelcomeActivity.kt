package fr.kodo.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.controller.ShoppingBasketAdapter
import fr.kodo.myapplication.model.Product
import fr.kodo.myapplication.controller.scan
import fr.kodo.myapplication.model.Session
import java.util.*
import kotlin.collections.ArrayList

private const val ACTION_SCAN = "com.google.zxing.client.android.SCAN"
class WelcomeActivity : AppCompatActivity() {

    var shoppingBasket = ArrayList<Product>()
    val shoppingBasketView by lazy { findViewById<RecyclerView>(R.id.welcome_rv_shopping_basket) }
    val btAddProduct by lazy { findViewById<Button>(R.id.welcome_bt_add_product) }
    val btCheckout by lazy { findViewById<Button>(R.id.welcome_bt_checkout) }
    val btLogout by lazy { findViewById<Button>(R.id.welcome_bt_logout) }

    val session by lazy{ Session(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        shoppingBasketView.adapter = ShoppingBasketAdapter(shoppingBasket)
        shoppingBasketView.layoutManager = LinearLayoutManager(this)

        btAddProduct.setOnClickListener { scan(this) }
        btCheckout.setOnClickListener { checkout() }
    }


    private fun checkout() {
        var totalPrice = 0.0
        for (product in shoppingBasket) {
            totalPrice += product.price * product.quantity
        }
        //message is formatted as "id,quantity,id,quantity..."
        var message = ""
        for (product in shoppingBasket) {
            message += "${product.id},${product.quantity},"
        }

        //Start OrderInfoFragment
        val orderInfoFragment = OrderInfoFragment()
        orderInfoFragment.arguments = Bundle().apply {
            putString("message", message)
            putDouble("totalPrice", totalPrice)
            putString("userUUID", session.getUserUUID())
        }
        orderInfoFragment.show(supportFragmentManager, "OrderInfoFragment")

        val nbProducts = shoppingBasket.size
        shoppingBasket.clear()
        shoppingBasketView.adapter?.notifyItemRangeRemoved(0, nbProducts)
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
                            UUID.randomUUID(),//TODO: get the id from the QRCode
                            result[0],
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

