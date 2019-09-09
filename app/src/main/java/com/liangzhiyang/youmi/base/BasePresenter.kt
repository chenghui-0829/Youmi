package com.liangzhiyang.youmi.base

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

open class BasePresenter<V : IBaseView> : IBasePresenter<V> {

    var mView: V? = null
    private var compositeDisposable = CompositeDisposable()

    override fun attachView(mRootView: V) {
        mView = mRootView
    }

    override fun detachView() {
        mView = null

        //activity结束时 取消订阅
        if (!compositeDisposable.isDisposed)
            compositeDisposable.clear()
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private val isViewAttached: Boolean
        get() = mView != null

    fun checkViewAttached(){
        if (!isViewAttached) throw MvpViewNotAttachedExeption()
    }

    fun getContext(): Context{
        return mView!!.context()
    }

    private class MvpViewNotAttachedExeption : RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")


}