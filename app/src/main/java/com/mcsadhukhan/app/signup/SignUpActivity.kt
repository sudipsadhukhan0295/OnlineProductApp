package com.mcsadhukhan.app.signup

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emi.manager.network.ApiResponse
import com.google.firebase.auth.*
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.ViewModelFactory
import com.mcsadhukhan.app.databinding.ActivitySignUpBinding
import com.mcsadhukhan.app.home.HomeActivity
import com.mcsadhukhan.app.model.User
import com.mcsadhukhan.app.util.BaseActivity
import com.mcsadhukhan.app.util.ConstantHelper


class SignUpActivity : BaseActivity(), OtpVerificationBottomSheet.ItemClickListener {

    var user = User()
    val phoneNo = ObservableField<String>()
    val isShowProgressbar: ObservableField<Boolean> = ObservableField(false)
    private lateinit var viewModel: SignUpViewModel
    private var verificationId = ""
    private val otpDialog: OtpVerificationBottomSheet = OtpVerificationBottomSheet.newInstance()

    private val mBinding: ActivitySignUpBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_sign_up) as ActivitySignUpBinding
    }

    private val getOtpObserver = Observer<ApiResponse<String>> { response: ApiResponse<String>? ->
        mBinding.apply {
            if (response !== null) {
                if (response.exception == null) {
                    isShowProgressbar.set(false)
                    otpDialog.show(supportFragmentManager, OtpVerificationBottomSheet.TAG)
                    verificationId = response.responseBody.toString()
                }
            }
        }
    }

    private val otpObserver =
        Observer<ApiResponse<FirebaseUser>> { response: ApiResponse<FirebaseUser>? ->
            mBinding.apply {
                if (response != null) {
                    if (response.exception == null) {
                        if (response.responseBody?.uid != null) {
                            otpDialog.dismiss()
                            val credential =
                                GoogleAuthProvider.getCredential(user.gmailIdToken, null)
                            viewModel.linkGoogleAuth(credential)
                                .observe(this@SignUpActivity, linkGoogleAuhObserver)
                        }
                    } else {
                        if (response.exception is FirebaseAuthInvalidCredentialsException) {
                            otpDialog.errorMessage("Please enter correct OTP.")
                            otpDialog.isProgress(false)
                        } else {
                            otpDialog.errorMessage("Something went wrong!")
                            errorSnackbar(rootView)
                        }
                    }
                }
            }
        }

    private val storeUserDetailObserver = Observer<ApiResponse<Boolean>> { response ->
        mBinding.apply {
            if (response != null) {
                if (response.exception == null) {
                    if (response.responseBody!!) {
                        val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                } else {
                    errorSnackbar(rootView)
                }
            }
        }
    }

    private val linkGoogleAuhObserver =
        Observer<ApiResponse<Boolean>> { response: ApiResponse<Boolean>? ->
            mBinding.apply {
                if (response != null) {
                    if (response.exception == null) {
                        if (response.responseBody != null && response.responseBody!!) {
                            val auth = FirebaseAuth.getInstance()
                            //user.isRegistered = true
                            viewModel.storeUserDetails(auth.currentUser?.uid!!, user)
                                .observe(this@SignUpActivity, storeUserDetailObserver)
                        }
                    } else {
                        errorSnackbar(rootView)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.activity = this
        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(SignUpViewModel::class.java)
        user = intent.getSerializableExtra(ConstantHelper.BUNDLE_LOGIN_USER_DETAIL) as User
        viewModel.userLiveData.observe(this, otpObserver)

    }

    fun sendOtp(phoneNo: String) {
        user.phoneNumber = "+91$phoneNo"
        isShowProgressbar.set(true)
        viewModel.sendOtp(user.phoneNumber!!)?.observe(this@SignUpActivity, getOtpObserver)
    }

    override fun onItemClick(value: String) {
        val credential =
            PhoneAuthProvider.getCredential(verificationId, value)
        viewModel.login(credential)
        otpDialog.isProgress(true)
    }

    fun back() {
        finish()
    }
}