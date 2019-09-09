package com.liangzhiyang.youmi.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.liangzhiyang.youmi.R
import kotlinx.android.synthetic.main.activity_agreement.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*

class AgreementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        toolbar_title.text = "有米钱袋服务协议"
        webView.loadUrl("file:////android_asset/youmi.html")
        toolbar_back.setOnClickListener { finish() }
    }

}