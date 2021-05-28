package com.mcsadhukhan.app.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.databinding.ViewProductListBinding
import com.mcsadhukhan.app.listener.OnImageClickListener
import com.mcsadhukhan.app.model.Product

class ProductListAdapter(
    private val context: Context,
    private val productList: MutableList<Product>
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    private lateinit var onImageClickListener: OnImageClickListener

    private lateinit var mBinding: ViewProductListBinding

    var isProductEditEnable = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.view_product_list, parent, false
        ) as ViewProductListBinding

        return ProductViewHolder(mBinding.root)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding?.product = productList[position]
        holder.binding?.adapter = this

    }

    fun onClick(url:String) {
        onImageClickListener.onImageClick(url)

    }

    fun setProductEnable(value:Boolean){
        isProductEditEnable = value
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun addAll(list: ArrayList<Product>) {
        productList.clear()
        productList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onImageClickListener: OnImageClickListener) {
        this.onImageClickListener = onImageClickListener
    }


    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = DataBindingUtil.bind<ViewProductListBinding>(itemView)

    }

}
