package com.liangzhiyang.youmi.base

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

interface IBasePresenter<in V: IBaseView> {

    fun attachView(mRootView: V)

    fun detachView()

}