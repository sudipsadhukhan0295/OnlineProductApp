package com.mcsadhukhan.app.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.login.LoginActivity
import com.mcsadhukhan.app.util.BaseActivity


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        splashViewModel.liveData.observe(this, {
            when (it) {
                is SplashViewModel.SplashState.MainActivity -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        })
    }

}