package com.mcsadhukhan.app.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emi.manager.network.ApiResponse
import com.mcsadhukhan.app.App
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.ViewModelFactory
import com.mcsadhukhan.app.adminproduct.ProductListActivity
import com.mcsadhukhan.app.databinding.ActivityHomeBinding
import com.mcsadhukhan.app.fullscreen.FullScreenImageActivity
import com.mcsadhukhan.app.listener.OnImageClickListener
import com.mcsadhukhan.app.model.Product
import com.mcsadhukhan.app.util.BaseActivity
import com.mcsadhukhan.app.util.ConstantHelper


class HomeActivity : BaseActivity() {

    companion object {
        private var isUserAdmin = false
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var productListAdapter: ProductListAdapter
    private val mBinding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home) as ActivityHomeBinding
    }
    private val productList = ArrayList<Product>()

    private val productListObserver = Observer<ApiResponse<ArrayList<Product>>> {
        if (it.exception == null) {
            if (it.responseBody != null) {
                productList.clear()
                productList.addAll(it.responseBody!!)
                productListAdapter.addAll(it.responseBody!!)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setSupportActionBar(mBinding.layoutContent.tbMain.tbMain)
        mBinding.activity = this
        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(HomeViewModel::class.java)

        val phoneNumber = App.firebaseAuth.currentUser?.phoneNumber
        isUserAdmin = phoneNumber.equals("+917003354782", ignoreCase = true)

        mBinding.apply {

            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            layoutContent.rvList.layoutManager = layoutManager
            layoutContent.rvList.setHasFixedSize(true)


            productListAdapter = ProductListAdapter(this@HomeActivity, mutableListOf())

            layoutContent.rvList.adapter = productListAdapter
            viewModel.getProductList().observe(this@HomeActivity, productListObserver)

            productListAdapter.setOnClickListener(object : OnImageClickListener {
                override fun onImageClick(url: String) {
                    val intent = Intent(applicationContext, FullScreenImageActivity::class.java)
                    intent.putExtra(ConstantHelper.BUNDLE_IMAGE_URL, url)
                    startActivity(intent)

                }

            })

            fabCall.setOnClickListener {
                if (isUserAdmin) {
                    val intent = Intent(this@HomeActivity,ProductListActivity::class.java)
                    intent.putExtra(ConstantHelper.BUNDLE_PRODUCT_LIST,productList)
                    startActivity(intent)
                } else {
                    call()
                }
            }
        }
    }

    private fun call() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:7003354782")
        startActivity(intent)
    }
}