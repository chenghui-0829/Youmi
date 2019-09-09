package com.liangzhiyang.youmi.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BaseActivity
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class FeedbackActivity : BaseActivity() {

    var count = 0

    override fun layoutId(): Int {
        return R.layout.activity_feedback
    }

    override fun initData() {
    }

    override fun initView() {
        toolbar_title.text = "意见反馈"

    }

    override fun loadData() {
    }

    override fun initListener() {
        super.initListener()
        feedback_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                this@FeedbackActivity.count = feedback_et.text.toString().length
                count_tv.text = count.toString() + "/400"
            }
        })
        sub_tv.setOnClickListener {
            subBack()
        }
    }

    fun subBack() {
        val phone = phone_et.text.toString()
        val content = feedback_et.text.toString()
        if (content.length < 10) {
            showMessage("最少提交10个字才能反馈哦")
            return
        }
        showProgressDialog("正在提交")
        val disposable = ApiManager.service.feedback(phone, content)
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    dismissProgressDialog()
                    if (it.isSuccess()) {
                        showMessage("提交反馈成功")
                        finish()
                    }
                }, {
                    dismissProgressDialog()
                    showMessage(getString(R.string.net_error))
                })
        addSubscription(disposable)
    }
}