package com.mcsadhukhan.app.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.databinding.ActivityProductDetailBinding
import com.mcsadhukhan.app.databinding.ActivityProductListBinding
import com.mcsadhukhan.app.fullscreen.FullScreenImageActivity
import com.mcsadhukhan.app.model.Product
import com.mcsadhukhan.app.util.BaseActivity
import com.mcsadhukhan.app.util.ConstantHelper

class ProductDetailActivity : BaseActivity() {
    private lateinit var productListAdapter: ProductPriceHistoryAdapter
    private val mBinding: ActivityProductDetailBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_product_detail
        ) as ActivityProductDetailBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val product = intent.getSerializableExtra(ConstantHelper.BUNDLE_PRODUCT) as Product?
        if (product!=null) {
            productListAdapter =
                ProductPriceHistoryAdapter(this@ProductDetailActivity, mutableListOf())

            mBinding.apply {
                activity = this@ProductDetailActivity
                this.product = product
                rvProductPriceList.adapter = productListAdapter
                productListAdapter.addAll(product.priceList)
            }
        }
    }

    fun openImage(imageUrl:String){
        val intent = Intent(this,FullScreenImageActivity::class.java)
        intent.putExtra(ConstantHelper.BUNDLE_IMAGE_URL,imageUrl)
        startActivity(intent)
    }
}