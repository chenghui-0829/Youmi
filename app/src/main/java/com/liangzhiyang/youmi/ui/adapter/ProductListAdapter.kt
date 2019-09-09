package com.liangzhiyang.youmi.ui.adapter

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.liangzhiyang.youmi.MyApp
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.base.adapter.CommonRecyclerAdapter
import com.liangzhiyang.youmi.base.adapter.CommonRecyclerHolder
import com.liangzhiyang.youmi.base.adapter.ListenerWithPosition
import com.liangzhiyang.youmi.model.ProductDetailBean
import com.liangzhiyang.youmi.ui.activity.WebViewActivity
import com.liangzhiyang.youmi.ui.activity.product_detail.ProductDetailActivity

/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */

class ProductListAdapter(context: Context?) : CommonRecyclerAdapter<ProductDetailBean>(context, null, R.layout.item_product_list),
        ListenerWithPosition.OnClickWithPositionListener<CommonRecyclerHolder> {

    var index = false

    constructor(context: Context?, index: Boolean) : this(context) {
        this.index = index
    }

    override fun convert(holder: CommonRecyclerHolder?, t: ProductDetailBean?) {
        t?.let {
            val imageView = holder?.getView<ImageView>(R.id.pdt_iv)
            Glide.with(mContext)
                    .load(t.productimg)
                    .apply(MyApp.options)
                    .into(imageView!!)

            holder?.let {
                holder.setTextViewText(R.id.pdt_name_tv, t.productName)
                holder.setTextViewText(R.id.pdt_amount_tv, t.maxLoanLimit.toDouble().toInt().toString())
                val deadlin = Html.fromHtml("贷款期限    " + t.startLoanDuration + t.startLoanDurationUnit + "-" + t.endLoanDuration + t.endLoanDurationUnit).toString()
                holder.setTextViewText(R.id.pdt_deadlin_tv, deadlin)
                holder.setTextViewText(R.id.pdt_limit_tv, "最大额度（" + t.maxLoanLimitUnit + "）")
                holder.setTextViewText(R.id.pdt_condition_tv, t.slogan)
                holder?.getView<RelativeLayout>(R.id.pdt_item).setOnClickListener {
                    ProductDetailActivity.startActivity(mContext, t.productId)
                }

                val tvApply = holder.getView<TextView>(R.id.pdt_apply_tv)
                val tvTag = holder.getView<TextView>(R.id.pdt_tag)
                if (index) {
                    tvApply.visibility = View.GONE
                    tvTag.visibility = View.VISIBLE
                    tvTag.text = t.productFeature
                } else {
                    tvApply.visibility = View.VISIBLE
                    tvTag.visibility = View.GONE
                    tvApply.setOnClickListener {
                        WebViewActivity.startActivity(mContext, t.forwardUrl, t.productId)
                    }
                }
            }!!
        }
    }

    override fun onClick(v: View?, position: Int, holder: CommonRecyclerHolder?) {

    }
}