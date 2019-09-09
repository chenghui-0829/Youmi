package com.liangzhiyang.youmi.view

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RadioGroup
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.utils.Config
import com.youmi.qiandai.listener.OnSortClickListener

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class SortPopupWindow(context: Context, onSortClickListener: OnSortClickListener) : PopupWindow() {

    init {
        contentView = View.inflate(context, R.layout.pop_sort, null)
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        isOutsideTouchable = true
        animationStyle = R.style.pop_anim
        setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.pop_bg)))

        val group = contentView.findViewById<RadioGroup>(R.id.radio_group)
        contentView.findViewById<View>(R.id.pop_rmtj).setOnClickListener {
            onSortClickListener.onSortClick(Config.PRODUCT_TYPE_HOT,group) }
        contentView.findViewById<View>(R.id.pop_zxsx).setOnClickListener {
            onSortClickListener.onSortClick(Config.PRODUCT_TYPE_NEW,group) }
        contentView.findViewById<View>(R.id.pop_xejs).setOnClickListener { onSortClickListener.onSortClick(Config.PRODUCT_TYPE_SMALL,group) }
        contentView.findViewById<View>(R.id.pop_dedx).setOnClickListener { onSortClickListener.onSortClick(Config.PRODUCT_TYPE_BIG,group) }
        contentView.findViewById<View>(R.id.out_view).setOnClickListener { dismiss() }
    }

    override fun showAsDropDown(anchor: View?) {
        if (Build.VERSION.SDK_INT >= 24){
            val rect = Rect()
            anchor?.getGlobalVisibleRect(rect)
            val h = anchor!!.resources.displayMetrics.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor)
    }

}