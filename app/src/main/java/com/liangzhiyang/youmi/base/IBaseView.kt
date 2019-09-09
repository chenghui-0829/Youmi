package com.liangzhiyang.youmi.base

import android.content.Context

/**
 * Created by zy on 2018/10/30
 * desc:
 */
interface IBaseView {

    fun showLoading()

    fun hideLoading()

    fun context(): Context

    fun showMessage(message: String)

    fun showProgressDialog(msg: String)

    fun dismissProgressDialog()

}