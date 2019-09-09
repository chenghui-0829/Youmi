package com.liangzhiyang.youmi.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.*
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BaseActivity
import com.liangzhiyang.youmi.utils.Config
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_webview.*

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class WebViewActivity : BaseActivity() {

    lateinit var mWebView: WebView //延迟初始化
    private var mUrl = ""
    private var productId = ""

    companion object {
        val WEB_URL = "web_url"
        fun startActivity(context: Context, url: String, productId: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(WEB_URL, url)
            intent.putExtra("PRODUCT_ID", productId)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_webview
    }

    override fun initData() {
        mUrl = intent.getStringExtra(WEB_URL)
    }

    override fun initView() {
//        showProgressDialog(getString(R.string.loading_msg))
        mWebView = webview
        initWebView()
    }

    private fun initWebView() {
        mWebView.webViewClient = MyWebClient()
        mWebView.webChromeClient = MyWebChrome()
        val setting = mWebView.settings
        setting.javaScriptCanOpenWindowsAutomatically = true
        setting.allowFileAccess = true
        setting.setSupportZoom(true)
        setting.javaScriptCanOpenWindowsAutomatically = true
        setting.cacheMode = WebSettings.LOAD_DEFAULT
        setting.setAppCacheEnabled(true)
        setting.javaScriptEnabled = true
        mWebView.setDownloadListener(object : DownloadListener {
            override fun onDownloadStart(url: String?, userAgent: String?, contentDisposition: String?, mimetype: String?, contentLength: Long) {
                val intent = Intent(Intent.ACTION_VIEW)
                val uri = Uri.parse(url)
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(uri)
                startActivity(intent)
            }
        })
        mWebView.loadUrl(mUrl)
    }

    override fun loadData() {
        productId = intent.getStringExtra("PRODUCT_ID")
        val mobile = Hawk.get<String>(Config.USER_PHONE,"")
        if (productId.isNotEmpty())
            dataReport(mobile, productId, "", "4")
    }

    //数据上报
    fun dataReport(mobile: String, productId: String, bannerUrl: String, type: String) {
        val disposable = ApiManager.service.dataReport("1", mobile, productId, bannerUrl, type)
                .compose(RxJavaHelper.observableToMain())
                .subscribe { }
        addSubscription(disposable)
    }

    private inner class MyWebChrome : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
//            showProgressDialog(newProgress.toString() + "%")
            progress.progress = newProgress
            if (newProgress == 100)
                progress.visibility=View.GONE
//                dismissProgressDialog()
        }

    }

    private inner class MyWebClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progress.visibility=View.GONE
//            dismissProgressDialog()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}