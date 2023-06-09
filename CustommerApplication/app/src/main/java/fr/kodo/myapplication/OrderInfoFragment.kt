package fr.kodo.myapplication

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import fr.kodo.myapplication.controller.Session
import fr.kodo.myapplication.crypto.KeyStoreUtils
import java.security.Signature
import kotlin.math.sign


class OrderInfoFragment: DialogFragment() {

    private val df = java.text.DecimalFormat("#.##")
    private val session by lazy {Session(requireActivity())}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_order_info, container, false)

        view.findViewById<TextView>(R.id.order_info_total_price).text = df.format(arguments?.getDouble("totalPrice")).toString()

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
                val useAccumulatedDiscount = view.findViewById<SwitchCompat>(R.id.order_info_use_accumulated_discount).isChecked

                var useAccumulatedDiscountInt = "0"

                if (useAccumulatedDiscount){
                    useAccumulatedDiscountInt = "1"
                }

                if (voucherId.isEmpty()){
                    voucherId = "0"
                }
                val newMessage = "$message$userUUID,$useAccumulatedDiscountInt,$voucherId"
                Log.e("message",newMessage)

                //generate the signature
                val privateKey = KeyStoreUtils.getKeyPair(session.getUsername())?.private

                val signature = Signature.getInstance("SHA256withRSA").run {
                    initSign(privateKey)
                    update(newMessage.toByteArray())
                    sign()
                }

                /*
                val strSignature = Base64.encodeToString(signature, Base64.DEFAULT)
                Log.e("signature",strSignature)

                val verify = Signature.getInstance("SHA256withRSA").run {
                    initVerify(KeyStoreUtils.getKeyPair(session.getUsername())?.public)
                    update(newMessage.toByteArray())
                    verify(Base64.decode(strSignature, Base64.DEFAULT))
                }
                Log.e("verify",verify.toString())*/

                //convert the signature to a string
                val signatureString = Base64.encodeToString(signature, Base64.DEFAULT)

                //add the signature to the message
                val newMessageWithSignature = "$newMessage,$signatureString"

                Log.d("OrderInfoFragment", "newMessageWithSignature: $newMessageWithSignature")

                //Start CheckoutQRCodeFragment
                val checkoutQRCodeFragment = CheckoutQRCodeFragment()
                checkoutQRCodeFragment.arguments = Bundle().apply {
                    putString("message", newMessageWithSignature)
                }

                checkoutQRCodeFragment.show(parentFragmentManager, "CheckoutQRCodeFragment")

                dismiss()

            }
        }

        return view
    }
}