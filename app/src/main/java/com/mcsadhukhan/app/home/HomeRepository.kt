package com.mcsadhukhan.app.home

import com.emi.manager.network.ApiResponse
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.mcsadhukhan.app.model.Dashboard
import com.mcsadhukhan.app.model.Product
import com.mcsadhukhan.app.util.ConstantHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeRepository {
    private val db = FirebaseFirestore.getInstance()
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getProductList(): ApiResponse<ArrayList<Product>> {
        return withContext(ioDispatcher) {
            try {
                val userQuerySnapshot =
                    db.collection("products").get().await()
                val list = ArrayList(userQuerySnapshot.toObjects<Product>())
                list.forEach {
                    it.priceList.sortByDescending { it["epochTime"] }
                }

                return@withContext ApiResponse(list)

            } catch (e: Exception) {
                return@withContext ApiResponse<ArrayList<Product>>(e)
            }
        }
    }

    suspend fun getDashboard(): ApiResponse<Dashboard> {
        return withContext(ioDispatcher) {
            try {
                val userQuerySnapshot =
                    db.collection("home").document("dashboard").get().await()
                val list = userQuerySnapshot.toObject(Dashboard::class.java)

                return@withContext ApiResponse(list)

            } catch (e: Exception) {
                return@withContext ApiResponse<Dashboard>(e)
            }
        }
    }

    suspend fun addProductDetails(product: Product): ApiResponse<Boolean> {

        return withContext(ioDispatcher) {
            try {
                val ref: DocumentReference =
                    db.collection(ConstantHelper.COLLECTION_PRODUCTS).document()
                val myId = ref.id
                product.id = myId
                db.collection(ConstantHelper.COLLECTION_PRODUCTS).document(myId).set(product)
                    .await()
                return@withContext ApiResponse(true)
            } catch (e: java.lang.Exception) {
                return@withContext ApiResponse<Boolean>(e)
            }
        }
    }

    suspend fun updateProductDetails(product: Product): ApiResponse<Boolean> {

        return withContext(ioDispatcher) {
            try {
                val map = hashMapOf<String,Any>("lastUpdate" to product.lastUpdate!!,
                "priceList" to FieldValue.arrayUnion(product.priceList[0]))
                db.collection(ConstantHelper.COLLECTION_PRODUCTS).document(product.id!!)
                    .update(map).await()
                return@withContext ApiResponse(true)
            } catch (e: Exception) {
                return@withContext ApiResponse<Boolean>(e)
            }

        }
    }
}