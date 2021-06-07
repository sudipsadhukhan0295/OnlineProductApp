package com.mcsadhukhan.app.adminproduct

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mcsadhukhan.app.R
import com.mcsadhukhan.app.ViewModelFactory
import com.mcsadhukhan.app.databinding.ActivityAdminAddProductBinding
import com.mcsadhukhan.app.home.HomeViewModel
import com.mcsadhukhan.app.model.Product
import com.mcsadhukhan.app.util.ConstantHelper
import com.mcsadhukhan.app.util.convertStringFormat
import kotlinx.android.synthetic.main.activity_admin_add_product.*
import kotlinx.android.synthetic.main.view_popup_checkbox.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AdminAddProductActivity : AppCompatActivity() {

    private val mBinding: ActivityAdminAddProductBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_admin_add_product
        ) as ActivityAdminAddProductBinding
    }
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isUpdateProduct = intent.getBooleanExtra(ConstantHelper.BUNDLE_IS_ADD_PRODUCT, false)
        val productDetail = intent.getSerializableExtra(ConstantHelper.BUNDLE_PRODUCT ) as Product?
        mBinding.apply {
            activity = this@AdminAddProductActivity
            product = productDetail
            isEditProduct = isUpdateProduct
            etQuantity.setOnClickListener {
                showSingleStringPopup(
                    arrayListOf("Tin", "Bottle", "Pouch"),
                    mBinding.etQuantity,
                    "Product Quantity Type:"
                )
            }

            viewModel = ViewModelProvider(
                this@AdminAddProductActivity,
                ViewModelFactory(this@AdminAddProductActivity)
            ).get(HomeViewModel::class.java)


            btnSubmit.setOnClickListener {
                val product = Product()
                val currentTime = Calendar.getInstance()
                product.unit = etQuantity.text?.toString()
                product.name = etName.text?.toString()
                product.lastUpdate = currentTime.convertStringFormat("dd-MMM-yyyy")
                val price: HashMap<String, String> = hashMapOf(
                    "epochTime" to currentTime.timeInMillis.toString(),
                    "price" to etPrice.text?.toString()!!,
                    "time" to product.lastUpdate!!
                )
                product.priceList.clear()
                product.priceList.add(price)
                if (isUpdateProduct && productDetail!=null) {
                    product.id = productDetail.id
                    viewModel.updateProductDetail(product).observe(this@AdminAddProductActivity, {
                        finish()
                    })

                } else {
                    viewModel.addProductDetail(product).observe(this@AdminAddProductActivity, {
                        finish()
                    })
                }

            }
        }
    }


    private fun createDialog(activity: Activity): Dialog {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }

    private fun showSingleStringPopup(value: ArrayList<String>, text: EditText, header: String) {
        val dialog = createDialog(this)
        val view = layoutInflater.inflate(R.layout.view_popup_checkbox, null)
        dialog.setContentView(view)
        view.tv_content.text = header
        view.rv_list.visibility = View.GONE
        value.forEach { i ->
            val textView = TextView(this)
            textView.text = i

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 36, 0, 0)
            textView.layoutParams = params

            textView.setOnClickListener {
                text.setText(textView.text)
                dialog.dismiss()
            }
            view.ll_view.addView(textView)
        }
        dialog.show()
    }
}