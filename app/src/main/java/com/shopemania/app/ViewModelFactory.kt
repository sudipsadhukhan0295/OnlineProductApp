package com.shopemania.app

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shopemania.app.login.LoginRepository
import com.shopemania.app.login.LoginViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(private val activity: Activity) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(LoginRepository(), activity)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}