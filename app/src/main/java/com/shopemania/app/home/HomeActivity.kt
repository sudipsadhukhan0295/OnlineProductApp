package com.shopemania.app.home

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emi.manager.network.ApiResponse
import com.shopemania.app.R
import com.shopemania.app.ViewModelFactory
import com.shopemania.app.databinding.ActivityHomeBinding
import com.shopemania.app.fullscreen.FullScreenImageActivity
import com.shopemania.app.listener.OnImageClickListener
import com.shopemania.app.model.Product
import com.shopemania.app.util.BaseActivity
import com.shopemania.app.util.ConstantHelper


class HomeActivity : BaseActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var productListAdapter: ProductListAdapter

    private val mBinding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home) as ActivityHomeBinding
    }

    private val productListObserver = Observer<ApiResponse<ArrayList<Product>>> {
        if (it.exception == null) {
            if (it.responseBody != null) {
                productListAdapter.addAll(it.responseBody!!)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(mBinding.layoutContent.tbMain.tbMain)
        mBinding.activity = this
        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(HomeViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        mBinding.apply {
            layoutContent.tbMain
            layoutContent.rvList.layoutManager = layoutManager
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
        }
    }
}