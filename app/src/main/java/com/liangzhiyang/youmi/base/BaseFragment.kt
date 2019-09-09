package com.liangzhiyang.youmi.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.liangzhiyang.youmi.MyApp
import com.liangzhiyang.youmi.view.MultipleStatusView
import com.liangzhiyang.youmi.view.UILoadingProgress
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

abstract class BaseFragment : Fragment() {

    /**
     * activity是否加载完成
     */
    private var isViewPreparer = false

    /**
     * 数据是否加载完成
     */
    private var hasLoadDate = false

    protected var mLayoutStatusView: MultipleStatusView? = null
    private var mProgressDialog: UILoadingProgress? = null
    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), null)
    }

    /**
     * 手动设置fragment属性
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
            lazyLoadDateIfPrepared()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPreparer = true
        initView()
        initListener()
        lazyLoadDateIfPrepared()
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    private fun lazyLoadDateIfPrepared() {
        if (userVisibleHint && isViewPreparer && !hasLoadDate) {
            lazyLoad()
            hasLoadDate = true
        }
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener { lazyLoad() }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initListener()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    fun hideLoading() {}

    fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoading() {}


    fun showProgressDialog(msg: String) {
        if (mProgressDialog == null)
            context().let {
                mProgressDialog = UILoadingProgress(context()!!)
            }
        mProgressDialog?.show(msg)
    }

    fun dismissProgressDialog() {
        mProgressDialog?.dismiss()
    }

    fun context(): Context {
        if (context == null)
            return MyApp.context
        else
            return context!!
    }

    override fun onDestroy() {
        mProgressDialog?.dismiss()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.clear()
        super.onDestroy()
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

}