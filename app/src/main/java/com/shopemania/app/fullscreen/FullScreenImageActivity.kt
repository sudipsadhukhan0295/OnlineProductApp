package com.shopemania.app.fullscreen

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.shopemania.app.R
import com.shopemania.app.databinding.ActivityProductImageBinding
import com.shopemania.app.util.BaseActivity
import com.shopemania.app.util.ConstantHelper

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