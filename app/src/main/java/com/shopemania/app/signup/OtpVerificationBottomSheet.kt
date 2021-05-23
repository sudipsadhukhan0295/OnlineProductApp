package com.shopemania.app.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopemania.app.R
import com.shopemania.app.customview.Pinview
import kotlinx.android.synthetic.main.view_otp.*
import kotlinx.android.synthetic.main.view_otp.view.*


class OtpVerificationBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private var mListener: ItemClickListener? = null
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(R.layout.view_otp, container, false)
        isProgress(false)
        return mView
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_verify).setOnClickListener(this)
        view.et_otp.setPinViewEventListener(object : Pinview.PinViewEventListener {
            override fun onDataEntered(pinview: Pinview, fromUser: Boolean) {
                view.btn_verify.isEnabled = pinview.value.length==6
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is ItemClickListener) {
            context
        } else {
            throw RuntimeException("$context must implement ItemClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun isProgress(isProgress: Boolean) {
        if (isProgress) {
            mView.pb_verify.visibility = View.VISIBLE
            mView.btn_verify.visibility = View.GONE
        } else {
            mView.pb_verify.visibility = View.GONE
            mView.btn_verify.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View) {
        mListener!!.onItemClick(et_otp.value)
    }

    fun errorMessage(msg: String) {
        mView.tv_error_message.text = msg
        mView.tv_error_message.visibility = View.VISIBLE
    }

    interface ItemClickListener {
        fun onItemClick(value: String)
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): OtpVerificationBottomSheet {
            return OtpVerificationBottomSheet()
        }
    }
}