package com.liangzhiyang.youmi.api

import com.liangzhiyang.youmi.MyApp
import com.liangzhiyang.youmi.base.IBaseView
import com.liangzhiyang.youmi.utils.NetworkUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

abstract class ApiResponse<T>(var view: IBaseView) : Observer<T> {

    abstract var mView: IBaseView

    init {
        mView = view
    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        onFailure(e)
    }

    override fun onComplete() {

    }

    fun hideLoading() {
        mView?.hideLoading()
    }


    fun onFailure(error: Throwable) {
        if (!NetworkUtil.isNetworkAvailable(MyApp.context)) {
            mView?.showMessage("网络不可用")
            return
        }
    }

    /**
     * 成功回调
     */
    abstract fun onSuccess(origin: T)

}