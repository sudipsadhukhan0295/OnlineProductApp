package com.mcsadhukhan.app.model

data class Dashboard(
    var adminNumber: ArrayList<String> = ArrayList(),
    var mustardOil: ArrayList<HashMap<String, Any>> = ArrayList(),
    var refinedOil: ArrayList<HashMap<String, Any>> = ArrayList(),
    val callNumber: String? = "",
    var productDetail:ProductDetail?= ProductDetail()
)

data class OilDetail(
    var date: String? = "",
    var price: String? = "",
    var unit: String? = ""
)

data class ProductDetail(
    var productQuantity:ArrayList<String> = ArrayList(),
    var productQuantityUnit:ArrayList<String> = ArrayList(),
    var productType:ArrayList<String> = ArrayList(),
    var productUnit:ArrayList<String> = ArrayList()
)
