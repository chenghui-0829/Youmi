package com.liangzhiyang.youmi.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BaseFragment
import com.liangzhiyang.youmi.model.InfoBean
import com.liangzhiyang.youmi.model.LoginEvent
import com.liangzhiyang.youmi.ui.activity.*
import com.liangzhiyang.youmi.ui.activity.login.LoginActivity
import com.liangzhiyang.youmi.utils.Config
import com.liangzhiyang.youmi.utils.CopyUtils
import com.liangzhiyang.youmi.view.MsgDialog
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_user_center.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

class MyFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_center
    }

    var mInfoBean: InfoBean ?= null

    override fun initView() {
        initLogin()
        toolbar_layout.setBackgroundResource(R.color.default_green)
        toolbar_back.visibility = View.GONE
        toolbar_title.text = "个人中心"
        toolbar_title.setTextColor(Color.WHITE)
    }

    override fun initListener() {
        person_layout.setOnClickListener {
            LoginActivity.startActivity(context())
        }

        user_loginout_tv.setOnClickListener {
            MsgDialog.Builder(context())
                    .setMsg("退出登录？")
                    .setCancle("取消")
                    .setOnCancleListener(object : MsgDialog.OnCancleListener {
                        override fun onCancle(dialog: Dialog) {
                            dialog.dismiss()
                        }
                    })
                    .setConfirm("退出")
                    .setOnConfirmListener(object : MsgDialog.OnConfirmListener {
                        override fun onConfirm(dialog: Dialog) {
                            dialog.dismiss()
                            Hawk.delete(Config.USER_SIGN)
                            loginView("", "")
                        }
                    })
                    .create()
                    .show()
        }

        user_guide_iv.setOnClickListener { startActivity(Intent(context(), NewGuideActivity::class.java)) }
        user_invitation_iv.setOnClickListener {
            dataReport("7")
            if (mInfoBean != null){
                ShareActivity.startActivity(activity!!)
            }
        }
        user_letter_iv.setOnClickListener {
            dataReport("8")
            if (mInfoBean != null && mInfoBean!!.customServiceWeChat.isNotEmpty()){
                CopyUtils.copyText(context(), mInfoBean!!.customServiceWeChat)
            }
        }
        user_feedback_iv.setOnClickListener { startActivity(Intent(context(), FeedbackActivity::class.java)) }
        user_about_iv.setOnClickListener {
            if (mInfoBean != null && mInfoBean!!.accountIntroduction.isNotEmpty()){
                val intent = Intent(context(), AboutWeActivity::class.java)
                intent.putExtra("about",mInfoBean!!.accountIntroduction)
                startActivity(intent)
            }

        }
        user_check_iv.setOnClickListener { startActivity(Intent(context(), CheckUpdateActivity::class.java)) }
        user_tel_iv.setOnClickListener {
            dataReport("8")
            if (mInfoBean != null && mInfoBean!!.customServiceTelephone.isNotEmpty()){
                val intent = Intent(Intent.ACTION_DIAL)
                val uri = Uri.parse("tel:" + mInfoBean!!.customServiceTelephone)
                intent.setData(uri)
                startActivity(intent)
            }
        }

        smart_refreshlayout.setOnRefreshListener {
            lazyLoad()
        }
    }

    override fun lazyLoad() {
        showProgressDialog(resources.getString(R.string.loading_msg))
        addSubscription(ApiManager.service.getInfo()
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    dismissProgressDialog()
                    smart_refreshlayout.finishRefresh()
                    if (it.isSuccess()){
                        mInfoBean = it.data
                        Hawk.put(Config.USER_INFO,it.data)
                        phone_tv.text = it.data.customServiceTelephone
                    }else{
                        getLoadData()
                    }

                },{
                    dismissProgressDialog()
                    smart_refreshlayout.finishRefresh()
                    showMessage(resources.getString(R.string.net_error))
                    getLoadData()
                }))
    }

    fun getLoadData(){
        val info = Hawk.get<InfoBean>(Config.USER_INFO, null)
        if (info != null){
            mInfoBean = info
            phone_tv.text = info.customServiceTelephone
        }
    }

    fun initLogin() {
        val phone = Hawk.get<String>(Config.USER_PHONE, "")
        val userSign = Hawk.get<String>(Config.USER_SIGN, "")
        loginView(phone, userSign)
    }

    fun loginView(phone: String, userSign: String) {
        if (userSign.isEmpty()) {
            //未登录
            user_name.text = "点击登录"
            user_loginout_tv.visibility = View.GONE
            startActivity(Intent(activity, LoginActivity::class.java))
        } else {
            user_name.text = phone
            user_loginout_tv.visibility = View.VISIBLE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun receiveLoginEvent(loginEvent: LoginEvent) {
        loginEvent.let {
            loginView(loginEvent.phone, loginEvent.userSign)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun dataReport(type: String) {
        val phone = Hawk.get<String>(Config.USER_PHONE, "")
        addSubscription(ApiManager.service.dataReport("1", phone, "", "", type)
                .compose(RxJavaHelper.observableToMain())
                .subscribe { })
    }

}