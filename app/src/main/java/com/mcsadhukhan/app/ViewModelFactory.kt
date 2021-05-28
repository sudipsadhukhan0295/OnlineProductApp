package com.mcsadhukhan.app

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mcsadhukhan.app.home.HomeRepository
import com.mcsadhukhan.app.home.HomeViewModel
import com.mcsadhukhan.app.login.LoginRepository
import com.mcsadhukhan.app.login.LoginViewModel
import com.mcsadhukhan.app.signup.SignUpRepository
import com.mcsadhukhan.app.signup.SignUpViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(private val activity: Activity) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(LoginRepository(), activity)
                isAssignableFrom(SignUpViewModel::class.java) ->
                    SignUpViewModel(SignUpRepository(), activity)
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(HomeRepository())
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}