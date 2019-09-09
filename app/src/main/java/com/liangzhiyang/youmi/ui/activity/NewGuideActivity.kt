package com.liangzhiyang.youmi.ui.activity

import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BaseActivity
import com.liangzhiyang.youmi.model.SwichEvent
import com.liangzhiyang.youmi.utils.Config
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_new_guide.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class NewGuideActivity : BaseActivity() {
    override fun loadData() {
        val phone = Hawk.get<String>(Config.USER_PHONE,"")
        addSubscription(ApiManager.service.dataReport("1", phone, "", "", "6")
                .compose(RxJavaHelper.observableToMain())
                .subscribe { })
    }

    override fun layoutId(): Int {
        return R.layout.activity_new_guide
    }

    override fun initData() {
    }

    override fun initView() {
        toolbar_title.text = "新手指南"
        toolbar_back.setOnClickListener { finish() }
        start_iv.setOnClickListener {
            EventBus.getDefault().post(SwichEvent(1))
            finish()
        }
    }

}