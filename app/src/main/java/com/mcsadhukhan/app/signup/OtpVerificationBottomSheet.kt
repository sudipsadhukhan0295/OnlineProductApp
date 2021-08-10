package com.mcsadhukhan.app.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.customview.Pinview
import com.mcsadhukhan.app.databinding.ViewOtpBinding


class OtpVerificationBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private var mListener: ItemClickListener? = null
    private lateinit var mBinding: ViewOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_otp, container, false
        ) as ViewOtpBinding
        isProgress(false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.btnVerify.setOnClickListener(this)
        mBinding.etOtp.setPinViewEventListener(object : Pinview.PinViewEventListener {
            override fun onDataEntered(pinview: Pinview, fromUser: Boolean) {
                mBinding.btnVerify.isEnabled = pinview.value.length == 6
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
            mBinding.pbVerify.visibility = View.VISIBLE
            mBinding.btnVerify.visibility = View.GONE
        } else {
            mBinding.pbVerify.visibility = View.GONE
            mBinding.btnVerify.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View) {
        mListener!!.onItemClick(mBinding.etOtp.value)
    }

    fun errorMessage(msg: String) {
        mBinding.tvErrorMessage.text = msg
        mBinding.tvErrorMessage.visibility = View.VISIBLE
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