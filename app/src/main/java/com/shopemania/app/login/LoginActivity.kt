package com.shopemania.app.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.shopemania.app.R
import com.shopemania.app.ViewModelFactory
import com.shopemania.app.databinding.ActivityLoginBinding
import com.shopemania.app.util.BaseActivity
import com.shopemania.app.util.ConstantHelper

class LoginActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    private val mBinding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_login) as ActivityLoginBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.activity = this
        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(LoginViewModel::class.java)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("AIzaSyA4tX99AblQ6KA-F5pGYhka46_oieEHipQ")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun onGoogleSignInClick() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, ConstantHelper.RC_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ConstantHelper.RC_GOOGLE_SIGN_IN) {
/*            progressbar.visibility= View.VISIBLE
            signInButton.visibility= View.INVISIBLE
            val result =
                Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }*/
        }
    }
}