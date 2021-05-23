package com.shopemania.app.model

import java.io.Serializable

data class Product(
    val id: String? = "",
    val name: String? = "",
    val type: String? = "",
    val lastUpdate:String? = "",
    val imageUrl: String? = "",
    val price: String? = ""
) : Serializable {
}