package fr.kodo.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.controller.ShoppingBasketAdapter
import fr.kodo.myapplication.model.Product

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

        btAddProduct.setOnClickListener {
            shoppingBasket.add(Product(shoppingBasket.size, "Product ${shoppingBasket.size}", 10.0, 1))
            shoppingBasketView.adapter?.notifyItemInserted(shoppingBasket.size - 1)
        }
    }
}