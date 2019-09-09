package com.liangzhiyang.youmi.ui.activity.product_detail
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bumptech.glide.Glide
import com.liangzhiyang.youmi.MyApp
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.base.BaseActivity
import com.liangzhiyang.youmi.model.ProductDetailBean
import com.liangzhiyang.youmi.ui.activity.WebViewActivity
import com.liangzhiyang.youmi.view.MsgDialog
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*

/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */

class ProductDetailActivity : BaseActivity(), IProductDetailContract.View {

    private val mPresenter = ProductDetailPresenter()
    private var productId = ""

    companion object {
        fun startActivity(context: Context, productId: String) {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("productId", productId)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_product_detail
    }

    override fun initData() {
        showProgressDialog(getString(R.string.loading_msg))
        productId = intent.getStringExtra("productId")
        if (productId.isEmpty()) {
            dismissProgressDialog()
            showMessage("产品信息异常")
            finish()
            return
        }
    }

    override fun initView() {
        toolbar_title.text = "产品详情"
        mPresenter.attachView(this)
    }

    override fun loadData() {
        mPresenter.getData(productId)
    }

    override fun setData(data: ProductDetailBean) {
        Glide.with(this)
                .load(data.productimg)
                .apply(MyApp.options)
                .into(pdt_iv)
        pdt_name_tv.text = data.productName
        pdt_slogan_number.text = data.slogan + getString(R.string.enter) + data.applyCount + "人已申请"
        pdt_max_limit.text = data.maxLoanLimit.toDouble().toInt().toString()    //最大额度
        pdt_max_limit_unit.text = "最大额度（" + data.maxLoanLimitUnit + "）" //最大额度单位
        pdt_deadlin_tv.text = data.startLoanDuration + data.startLoanDurationUnit + "-" + data.endLoanDuration + data.endLoanDurationUnit //7天-1年
        pdt_rate.text = data.interestRate + "%"
        pdt_rate_unit.text = "参考"+data.interestRateUnit
        data.applyConditions?.let {
            var strCondition = ""
            val conditions = data.applyConditions
            for (index in conditions.indices) {
                if (index == conditions.size - 1) {
                    strCondition += (index + 1).toString() + "、" + conditions[index]
                } else {
                    strCondition += (index + 1).toString() + "、" + conditions[index] + getString(R.string.enter)
                }
            }
            pdt_condition_tv.text = strCondition
        }
        data.applyMaterials?.let {
            var strMaterial = ""
            val materials = data.applyMaterials
            for (index in materials.indices) {
                if (index == materials.size - 1) {
                    strMaterial += (index + 1).toString() + "、" + materials[index]
                } else {
                    strMaterial += (index + 1).toString() + "、" + materials[index] + getString(R.string.enter)
                }
            }
            pdt_materials_tv.text = strMaterial
        }
        val phone = "机构电话 \n" + data.agencyMobile
        pdt_phone_tv.text = "机构电话 " + data.agencyMobile
        pdt_phone_tv.setOnClickListener {
            MsgDialog.Builder(this)
                    .setMsg(phone)
                    .setCancle("取消")
                    .setConfirm("呼叫")
                    .setOnConfirmListener(object : MsgDialog.OnConfirmListener{
                        override fun onConfirm(dialog: Dialog) {
                            val intent = Intent(Intent.ACTION_DIAL)//Intent.ACTION_CALL
                            val uri = Uri.parse("tel:" + data.agencyMobile)
                            intent.setData(uri)
                            startActivity(intent)
                            dialog.dismiss()
                        }
                    })
                    .setOnCancleListener(object : MsgDialog.OnCancleListener{
                        override fun onCancle(dialog: Dialog) {
                            dialog.dismiss()
                        }
                    })
                    .create()
                    .show()
        }
        pdt_apply_tv.setOnClickListener {
            WebViewActivity.startActivity(this,data.forwardUrl,data.productId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}