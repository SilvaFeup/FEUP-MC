package fr.kodo.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.kodo.myapplication.controller.ShoppingBasketAdapter
import fr.kodo.myapplication.model.Product
import fr.kodo.myapplication.controller.scan
import fr.kodo.myapplication.model.Session
import java.util.*
import kotlin.collections.ArrayList

class WelcomeActivity : AppCompatActivity() {

    private var shoppingBasket = ArrayList<Product>()
    private val shoppingBasketView by lazy { findViewById<RecyclerView>(R.id.welcome_rv_shopping_basket) }
    private val btAddProduct by lazy { findViewById<Button>(R.id.welcome_bt_add_product) }
    private val btCheckout by lazy { findViewById<Button>(R.id.welcome_bt_checkout) }
    private val emptyMessage by lazy { findViewById<TextView>(R.id.welcome_tv_empty) }

    private val session by lazy{ Session(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)



        emptyMessage.visibility = TextView.VISIBLE

        shoppingBasketView.adapter = ShoppingBasketAdapter(shoppingBasket, emptyMessage)
        shoppingBasketView.layoutManager = LinearLayoutManager(this)

        btAddProduct.setOnClickListener { scan(this) }
        btCheckout.setOnClickListener { checkout() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.welcome, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_welcome_logout){
            session.logout()
            finish()
        }
        if (item.itemId == R.id.menu_welcome_see_transactions){
            //TODO
            val intent = Intent(this, PastTransactionActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.menu_welcome_see_vouchers){
            val seeVoucherFragment = SeeVoucherFragment()
            seeVoucherFragment.show(supportFragmentManager, "SeeVoucherFragment")
        }
        return super.onOptionsItemSelected(item)
    }


    private fun checkout() {
        if (shoppingBasket.isEmpty()){
            Toast.makeText(this, "Your shopping basket is empty", Toast.LENGTH_SHORT).show()
            return
        }
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

        supportFragmentManager.setFragmentResultListener("checkout", this) { _, bundle ->
            val result = bundle.getString("checkout")
            if (result == "success") {
                val nbProducts = shoppingBasket.size
                shoppingBasket.clear()
                shoppingBasketView.adapter?.notifyItemRangeRemoved(0, nbProducts)
            }
            else{
                Log.println(Log.ERROR, "WelcomeActivity", result?:"")
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                val product =data?.getStringExtra("SCAN_RESULT")?:""
                if (product != "") {

                    val result = product.split(",")

                    try{
                        val newProduct = Product(UUID.fromString(result[0]), result[1], result[2].toDouble())

                        //check if product already in shopping basket, no matter the quantity
                        for (item in shoppingBasket){
                            if (item.id == newProduct.id){
                                item.quantity += newProduct.quantity
                                shoppingBasketView.adapter?.notifyItemChanged(shoppingBasket.indexOf(item))
                                return
                            }
                        }
                        shoppingBasket.add(newProduct)
                        shoppingBasketView.adapter?.notifyItemInserted(shoppingBasket.size - 1)
                        emptyMessage.visibility = TextView.GONE

                    }
                    catch (e: Exception){
                        Toast.makeText(this, "Invalid QR code", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}

