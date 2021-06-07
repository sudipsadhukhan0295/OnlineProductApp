package com.mcsadhukhan.app.fullscreen

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.databinding.ActivityProductImageBinding
import com.mcsadhukhan.app.util.BaseActivity
import com.mcsadhukhan.app.util.ConstantHelper

class FullScreenImageActivity : BaseActivity(){

    private val mProductImageBinding: ActivityProductImageBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_product_image) as ActivityProductImageBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = intent.getStringExtra(ConstantHelper.BUNDLE_IMAGE_URL)
        mProductImageBinding.imageUrl = imageUrl
    }
}