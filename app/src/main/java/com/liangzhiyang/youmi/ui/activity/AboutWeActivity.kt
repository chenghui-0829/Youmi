package com.liangzhiyang.youmi.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.liangzhiyang.youmi.R
import kotlinx.android.synthetic.main.activity_about_we.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class AboutWeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_we)
        toolbar_title.text = "关于我们"
        toolbar_back.setOnClickListener { finish() }

        val about = intent.getStringExtra("about")
        if (about.isNotEmpty())
            aboutwe_tv.text = resources.getString(R.string.suojin)+about
    }

}