package com.shopemania.app

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shopemania.app.home.HomeRepository
import com.shopemania.app.home.HomeViewModel
import com.shopemania.app.login.LoginRepository
import com.shopemania.app.login.LoginViewModel
import com.shopemania.app.signup.SignUpRepository
import com.shopemania.app.signup.SignUpViewModel


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