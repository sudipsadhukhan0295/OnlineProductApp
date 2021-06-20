package com.mcsadhukhan.app.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcsadhukhan.app.network.ApiResponse
import com.mcsadhukhan.app.model.Dashboard
import com.mcsadhukhan.app.model.Product
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

    fun getDashboardData(): MutableLiveData<ApiResponse<Dashboard>> {

        val data = MutableLiveData<ApiResponse<Dashboard>>()
        viewModelScope.launch {
            repository.getDashboard().let {
                data.value = it
            }
        }
        return data

    }



    fun addProductDetail(product: Product): MutableLiveData<ApiResponse<Boolean>> {

        val data = MutableLiveData<ApiResponse<Boolean>>()
        viewModelScope.launch {
            repository.addProductDetails(product).let {
                data.value = it
            }
        }
        return data
    }


    fun updateProductDetail(product: Product): MutableLiveData<ApiResponse<Boolean>> {

        val data = MutableLiveData<ApiResponse<Boolean>>()
        viewModelScope.launch {
            repository.updateProductDetails(product).let {
                data.value = it
            }
        }
        return data
    }

}