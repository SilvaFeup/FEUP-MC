package fr.kodo.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.DialogFragment


class OrderInfoFragment: DialogFragment() {

    private val df = java.text.DecimalFormat("#.##")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_order_info, container, false)

        view.findViewById<TextView>(R.id.orderinfo_total_price).text = df.format(arguments?.getDouble("totalPrice")).toString()

        view.findViewById<Button>(R.id.order_info_cancel_button).setOnClickListener {
            dismiss()
        }

        view.findViewById<Button>(R.id.order_info_confirm_button).setOnClickListener {
            arguments?.let {
                val message = it.getString("message")
                val totalPrice = it.getDouble("totalPrice")
                val userUUID = it.getString("userUUID")

                Log.println(Log.WARN, "OrderInfoFragment", "message: $message totalPrice: $totalPrice userUUID: $userUUID")

                //the message is formatted as "id,quantity,id,quantity...,UserUUID,useAccumulatedDiscount,VoucherId"
                var voucherId = view.findViewById<EditText>(R.id.order_info_voucher_id).text.toString()
                val useAccumulatedDiscount = view.findViewById<Switch>(R.id.order_info_use_accumulated_discount).isChecked

                var useAccumulatedDiscountInt = "0"

                if (useAccumulatedDiscount){
                    useAccumulatedDiscountInt = "1"
                }

                if (voucherId.isEmpty()){
                    voucherId = "0"
                }
                val newMessage = "$message$userUUID,$useAccumulatedDiscountInt,$voucherId"

                //Start CheckoutQRCodeFragment
                val checkoutQRCodeFragment = CheckoutQRCodeFragment()
                checkoutQRCodeFragment.arguments = Bundle().apply {
                    putString("message", newMessage)
                }

                checkoutQRCodeFragment.show(parentFragmentManager, "CheckoutQRCodeFragment")

                dismiss()

            }
        }

        return view
    }
}