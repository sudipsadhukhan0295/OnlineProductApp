package com.mcsadhukhan.app.adminproduct

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mcsadhukhan.app.App
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.databinding.ActivityProductListBinding
import com.mcsadhukhan.app.fullscreen.FullScreenImageActivity
import com.mcsadhukhan.app.home.ProductListAdapter
import com.mcsadhukhan.app.listener.OnImageClickListener
import com.mcsadhukhan.app.listener.OnProductClickListener
import com.mcsadhukhan.app.model.Product
import com.mcsadhukhan.app.util.BaseActivity
import com.mcsadhukhan.app.util.ConstantHelper

class ProductListActivity : BaseActivity() {

    private lateinit var productListAdapter: ProductListAdapter
    private val mBinding: ActivityProductListBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_product_list
        ) as ActivityProductListBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(mBinding.tbMain.tbMain)

        productListAdapter = ProductListAdapter(this@ProductListActivity, mutableListOf())
        productListAdapter.isProductEditEnable = true


        mBinding.apply {
            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            rvList.layoutManager = layoutManager
            rvList.setHasFixedSize(true)
            rvList.layoutManager = layoutManager
            rvList.adapter = productListAdapter

            fabAddProduct.setOnClickListener {
                startActivity(Intent(this@ProductListActivity,AdminAddProductActivity::class.java))
            }
        }
        val productList = intent.getSerializableExtra(ConstantHelper.BUNDLE_PRODUCT_LIST) as ArrayList<Product>?
        if (productList != null) {
            productListAdapter.addAll(productList)
        }


        productListAdapter.setOnClickListener(object : OnProductClickListener{
            override fun onClick(product: Product) {
                val intent = Intent(applicationContext, AdminAddProductActivity::class.java)
                intent.putExtra(ConstantHelper.BUNDLE_IS_ADD_PRODUCT, true)
                intent.putExtra(ConstantHelper.BUNDLE_PRODUCT, product)
                startActivity(intent)
            }
        })

    }


}