package com.mcsadhukhan.app.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mcsadhukhan.app.network.ApiResponse
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.ViewModelFactory
import com.mcsadhukhan.app.databinding.ActivityLoginBinding
import com.mcsadhukhan.app.home.HomeActivity
import com.mcsadhukhan.app.model.User
import com.mcsadhukhan.app.signup.SignUpActivity
import com.mcsadhukhan.app.util.BaseActivity
import com.mcsadhukhan.app.util.ConstantHelper
import com.mcsadhukhan.app.util.ConstantHelper.RC_SIGN_IN
import com.mcsadhukhan.app.util.ConstantHelper.REQUESTIDTOKEN

class LoginActivity : BaseActivity() {

    companion object {
        private val TAG = LoginActivity::class.java.name
    }

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var user = User()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var credential: AuthCredential
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val gmailLoginObserver = Observer<ApiResponse<Boolean>> { response ->
        closeLoader()
        if (response != null) {
            if (response.exception == null) {
                if (response.responseBody!!) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Log.e(TAG, response.exception.toString())
                firebaseAuth.signOut()
                errorSnackbar(mBinding.rootView)
            }
        } else {
            errorSnackbar(mBinding.rootView)
        }
    }

    private val checkIfUserExistObserver = Observer<ApiResponse<Boolean>> { response ->
        closeLoader()
        if (response != null) {
            if (response.exception == null) {
                mGoogleSignInClient.signOut()
                if (response.responseBody!!) {
                    val intent = Intent(this, SignUpActivity::class.java)
                    intent.putExtra(ConstantHelper.BUNDLE_LOGIN_USER_DETAIL, user)
                    startActivity(intent)
                } else {
                    viewModel.loginUsingGmail(credential).observe(this, gmailLoginObserver)
                }
            } else {
                Log.e(TAG, response.exception.toString())
            }
        } else {
            errorSnackbar(mBinding.rootView)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(LoginViewModel::class.java)
        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.activity = this
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(REQUESTIDTOKEN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    fun onGoogleSignInClick() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mBinding.progressbar.visibility = View.VISIBLE
            mBinding.signInButton.visibility = View.INVISIBLE
            val result =
                Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result?.isSuccess!!) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            user.socialMediaId = account?.id!!
            user.emailId = account.email!!
            user.name = account.displayName.toString()
            user.gmailIdToken = account.idToken.toString()
            credential = GoogleAuthProvider.getCredential(user.gmailIdToken, null)
            viewModel.checkIfEmailExist(user.socialMediaId!!)
                .observe(this, checkIfUserExistObserver)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun closeLoader() {
        mBinding.progressbar.visibility = View.GONE
        mBinding.signInButton.visibility = View.VISIBLE
    }

}
