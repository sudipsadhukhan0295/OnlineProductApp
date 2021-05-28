package com.mcsadhukhan.app.model

data class Dashboard(
    var adminNumber :ArrayList<String> = ArrayList(),
    var mustardOil: ArrayList<HashMap<String,Any>> = ArrayList(),
    var refinedOil: ArrayList<HashMap<String,Any>> = ArrayList()
)
