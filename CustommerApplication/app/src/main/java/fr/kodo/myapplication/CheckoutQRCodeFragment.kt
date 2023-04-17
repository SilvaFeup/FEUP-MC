package fr.kodo.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import fr.kodo.myapplication.controller.generateQRCode

class CheckoutQRCodeFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checkout_qr_code, container, false)

        view.findViewById<ImageView>(R.id.checkout_qr_code_iv_qr_code).setImageBitmap(
            generateQRCode(arguments?.getString("message") ?: ""))

        view.findViewById<Button>(R.id.checkout_qr_code_bt_done).setOnClickListener {
            parentFragmentManager.setFragmentResult("checkout", Bundle().apply {
                putString("checkout", "success")
            })
            dismiss()
        }

        view.findViewById<Button>(R.id.checkout_qr_code_bt_cancel).setOnClickListener{
            dismiss()
        }

        return view
    }
}