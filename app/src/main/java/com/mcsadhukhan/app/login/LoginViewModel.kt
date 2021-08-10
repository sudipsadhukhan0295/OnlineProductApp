package com.mcsadhukhan.app.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcsadhukhan.app.network.ApiResponse
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository,private val activity: Activity): ViewModel() {

    fun checkIfEmailExist(socialMediaId: String): MutableLiveData<ApiResponse<Boolean>> {
        val data = MutableLiveData<ApiResponse<Boolean>>()
        viewModelScope.launch {
            repository.checkIfEmailExist(socialMediaId).let {
                data.value = it
            }
        }
        return data
    }

    fun loginUsingGmail(credential: AuthCredential): MutableLiveData<ApiResponse<Boolean>> {
        val data = MutableLiveData<ApiResponse<Boolean>>()
        viewModelScope.launch {
            repository.getGmailLogin(credential,activity).let {
                data.value = it
            }
        }
        return data
    }
}