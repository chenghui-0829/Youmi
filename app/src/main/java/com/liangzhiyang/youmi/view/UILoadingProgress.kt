package com.liangzhiyang.youmi.view

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.liangzhiyang.youmi.R
import kotlinx.android.synthetic.main.widget_loading_progress.*

/**
 * Created by zy
 * Date: 2019/2/20
 * desc:
 */
 
class UILoadingProgress(context: Context,themeResId: Int) : Dialog(context) {

    constructor(context: Context) : this(context, R.style.loading_style)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(false)
        window.setBackgroundDrawableResource(R.color.transparent)
        window.setDimAmount(0f)
        setContentView(R.layout.widget_loading_progress)
    }

    fun show(msg: String) {
        super.show()
        message_tv.text = msg
    }

}
