package com.shopemania.app.home

import androidx.lifecycle.MutableLiveData
import com.emi.manager.network.ApiResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObjects
import com.shopemania.app.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getProductList(): ApiResponse<ArrayList<Product>>{
        return withContext(Dispatchers.IO) {
            try {
                val userQuerySnapshot =
                    db.collection("products").get().await()
                val list = ArrayList(userQuerySnapshot.toObjects<Product>())

                return@withContext ApiResponse(list)

            } catch (e: Exception) {
                return@withContext ApiResponse<ArrayList<Product>>(e)
            }
        }
    }
}