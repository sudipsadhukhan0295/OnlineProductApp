package com.shopemania.app.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emi.manager.network.ApiResponse
import com.shopemania.app.model.Product
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    fun getProductList(): MutableLiveData<ApiResponse<ArrayList<Product>>> {

        val data = MutableLiveData<ApiResponse<ArrayList<Product>>>()
        viewModelScope.launch {
            repository.getProductList().let {
                data.value = it
            }
        }
        return data

    }

}