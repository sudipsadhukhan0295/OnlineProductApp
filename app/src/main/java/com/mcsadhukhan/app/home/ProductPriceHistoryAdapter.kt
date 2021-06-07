package com.mcsadhukhan.app.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.databinding.ViewProductListBinding
import com.mcsadhukhan.app.databinding.ViewProductPriceBinding
import com.mcsadhukhan.app.model.Product

class ProductPriceHistoryAdapter(
    private val context: Context,
    private val productList: MutableList<HashMap<String, String>>
) : RecyclerView.Adapter<ProductPriceHistoryAdapter.ProductViewHolder>() {

    private lateinit var mBinding: ViewProductPriceBinding


    class ProductViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = DataBindingUtil.bind<ViewProductPriceBinding>(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.view_product_price, parent, false
        ) as ViewProductPriceBinding

        return ProductViewHolder(mBinding.root)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding?.apply {
            tvPrice.text = productList[position]["price"]
            tvDate.text = productList[position]["time"]
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun addAll(product:ArrayList<HashMap<String, String>>){
        this.productList.clear()
        this.productList.addAll(product)
    }
}