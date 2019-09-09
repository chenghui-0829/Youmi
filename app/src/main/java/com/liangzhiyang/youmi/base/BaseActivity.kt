package com.liangzhiyang.youmi.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.jaeger.library.StatusBarUtil
import com.liangzhiyang.youmi.view.MultipleStatusView
import com.liangzhiyang.youmi.view.UILoadingProgress
import com.umeng.message.PushAgent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_base_toolbar.*

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

abstract class BaseActivity : AppCompatActivity() {

    protected var mLayoutStatusView: MultipleStatusView? = null
    private var mProgressDialog: UILoadingProgress? = null
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //推送
        PushAgent.getInstance(this).onAppStart()

        setContentView(layoutId())
        initData()
        initView()
        initListener()
        loadData()
        StatusBarUtil.setLightMode(this)
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    val mRetryClickListener: View.OnClickListener = View.OnClickListener { loadData() }


    abstract fun layoutId(): Int

    abstract fun initData()

    abstract fun initView()

    open fun initListener() {
        toolbar_back?.setOnClickListener { finish() }
    }

    abstract fun loadData()

    fun hideLoading() {}

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoading() {}

    fun context(): Context {
        return applicationContext
    }

    fun showProgressDialog(msg: String) {
        if (mProgressDialog == null)
            mProgressDialog = UILoadingProgress(this)
        mProgressDialog?.show(msg)
    }

    fun dismissProgressDialog(){
        mProgressDialog?.dismiss()
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        //activity结束时 取消订阅
        if (!compositeDisposable.isDisposed)
            compositeDisposable.clear()
        mProgressDialog?.dismiss()
        super.onDestroy()
    }

}