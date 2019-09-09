package com.liangzhiyang.youmi.view

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.listener.OnFeatureClickListener

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class FeaturePopupWindow(context: Context, onFeatureClickListener: OnFeatureClickListener) : PopupWindow() {

    init {
        contentView = View.inflate(context, R.layout.pop_feature, null)
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        isOutsideTouchable = true
        animationStyle = R.style.pop_anim
        setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.pop_bg)))

        contentView.findViewById<MultiLineRadioGroup>(R.id.feature_rg).setOnCheckChangedListener { group, position, checked ->
            group.setItemChecked(position)
            onFeatureClickListener.onFeatureClick(group, position, checked)
        }
        contentView.findViewById<View>(R.id.out_view).setOnClickListener { dismiss() }
    }

    override fun showAsDropDown(anchor: View?) {
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            anchor?.getGlobalVisibleRect(rect)
            val h = anchor!!.resources.displayMetrics.heightPixels - rect.bottom
            height = h
        }
        super.showAsDropDown(anchor)
    }

}