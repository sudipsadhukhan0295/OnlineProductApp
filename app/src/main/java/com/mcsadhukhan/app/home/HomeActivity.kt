package com.mcsadhukhan.app.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mcsadhukhan.app.network.ApiResponse
import com.mcsadhukhan.app.App
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.ViewModelFactory
import com.mcsadhukhan.app.adminproduct.ProductListActivity
import com.mcsadhukhan.app.databinding.ActivityHomeBinding
import com.mcsadhukhan.app.listener.OnProductClickListener
import com.mcsadhukhan.app.model.Dashboard
import com.mcsadhukhan.app.model.Product
import com.mcsadhukhan.app.util.BaseActivity
import com.mcsadhukhan.app.util.ConstantHelper

class HomeActivity : BaseActivity() {

    companion object {
        private var isUserAdmin = false
    }

    private var dashboard: Dashboard? = Dashboard()

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

    private val dashboardObserver = Observer<ApiResponse<Dashboard>> {
        if (it.exception == null) {
            if (it.responseBody != null) {
                dashboard = it.responseBody
                if (dashboard?.productDetail != null) {
                    ConstantHelper.productDetail = dashboard?.productDetail!!
                }
                val admin = dashboard?.adminNumber
                val phoneNumber = App.firebaseAuth.currentUser?.phoneNumber
                if (admin != null) {
                    for (number in admin) {
                        isUserAdmin = number.equals(phoneNumber, ignoreCase = true)
                        if (isUserAdmin) break
                    }
                }
                val mustardOil = dashboard?.mustardOil?.get(0)
                val refinedOil = dashboard?.refinedOil?.get(0)
                mBinding.layoutContent.apply {
                    tvMoilPrice.text = "Price: Rs.${mustardOil?.get("price") as String}/${mustardOil.get("unit") as String}"
                    tvRoilPrice.text = "Price: Rs.${refinedOil?.get("price") as String}/${mustardOil.get("unit") as String}"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.activity = this
        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(HomeViewModel::class.java)
        viewModel.getDashboardData().observe(this, dashboardObserver)


        mBinding.apply {

            layoutContent.apply {
                val layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                layoutManager.gapStrategy =
                    StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                rvList.layoutManager = layoutManager
                rvList.setHasFixedSize(true)
                val userDetail = App.firebaseAuth.currentUser

                if (userDetail?.displayName != null) {
                    tvHelloUser.text = "Hello ${userDetail.displayName!!.split(" ")[0]}"
                }


                productListAdapter = ProductListAdapter(this@HomeActivity, mutableListOf())

                rvList.adapter = productListAdapter
                viewModel.getProductList().observe(this@HomeActivity, productListObserver)

                productListAdapter.setOnClickListener(object : OnProductClickListener {
                    override fun onClick(product: Product) {
                        val intent = Intent(applicationContext, ProductDetailActivity::class.java)
                        intent.putExtra(ConstantHelper.BUNDLE_PRODUCT, product)
                        startActivity(intent)
                    }
                })
            }

            fabCall.setOnClickListener {
                if (isUserAdmin) {
                    val intent = Intent(this@HomeActivity, ProductListActivity::class.java)
                    intent.putExtra(ConstantHelper.BUNDLE_PRODUCT_LIST, productList)
                    startActivity(intent)
                } else {
                    call()
                }
            }
        }
    }

    private fun call() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${dashboard?.callNumber}")
        startActivity(intent)
    }
}