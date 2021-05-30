package com.mcsadhukhan.app.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Product(
    var id: String? = "",
    var name: String? = "",
    var lastUpdate: String? = Calendar.getInstance().time.toString(),
    var priceList: ArrayList<HashMap<String, String>> = ArrayList(),
    var unit: String? = "",
    var imageUrl: String? = "",
    var oilType: String? = ""
) : Serializable
