package com.liangzhiyang.youmi.ui.activity.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.base.BaseActivity
import com.liangzhiyang.youmi.model.LoginBean
import com.liangzhiyang.youmi.model.LoginEvent
import com.liangzhiyang.youmi.ui.MainActivity
import com.liangzhiyang.youmi.ui.activity.AgreementActivity
import com.liangzhiyang.youmi.utils.Config
import com.liangzhiyang.youmi.utils.LocationUtils
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus


/**
 * Created by zy
 * Date: 2019/2/18
 * desc:
 */

class LoginActivity : BaseActivity(), ILoginContract.View {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private val mPresenter by lazy { LoginPresenter() }
    private var phoneEmpty: Boolean = true
    private var codeEmpty: Boolean = true
    private var mLocationUtils = LocationUtils(this, this)

    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        initPermission()
    }

    override fun initView() {
        mPresenter.attachView(this)
    }

    override fun initListener() {
        super.initListener()
        send_code_tv.setOnClickListener {
            mPresenter.sendCode(phone_et.text.toString().trim())
        }

        login_tv.setOnClickListener {
            mPresenter.login(phone_et.text.toString().trim(), code_et.text.toString().trim(),mLocationUtils.mCity)
        }

        agreement_tv.setOnClickListener {
            startActivity(Intent(this, AgreementActivity::class.java))
        }

        phone_et.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                phoneEmpty = s.isNullOrEmpty()
                setLoginBg()
                if (phone_et.text.toString().length == 11)
                    reportType("2")
            }
        })

        code_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                codeEmpty = s.isNullOrEmpty()
                setLoginBg()
                if (code_et.text.length == 4)
                    reportType("3")
            }

        })

    }

    /**
     * 设置登录背景
     */
    fun setLoginBg() {
        if (!phoneEmpty && !codeEmpty) login_tv.setBackgroundResource(R.drawable.shape_green_bg) else login_tv.setBackgroundResource(R.drawable.shape_gray_bg)
    }


    override fun loadData() {

    }

    override fun sendSucceed() {
        send_code_tv.setBackgroundResource(R.drawable.shape_gray_bg)
    }

    override fun timer(desc: String) {
        send_code_tv.text = desc
    }

    override fun resendCode() {
        send_code_tv.text = "重新发送"
        send_code_tv.setBackgroundResource(R.drawable.shape_green_bg)
    }

    override fun loginSuccess(loginBean: LoginBean, phone: String) {
        Hawk.put(Config.USER_SIGN, loginBean.userSign)
        Hawk.put(Config.USER_PHONE, phone)
        EventBus.getDefault().postSticky(LoginEvent(phone, loginBean.userSign))
        showMessage("登录成功")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            EventBus.getDefault().post(FinishEvent())
//            ActivityManager.getInstance().AppExit(this)
            finish()
        }
        return true
    }

    override fun reportType(type: String) {
        mPresenter.dataReport(phone_et.text.toString(), "", "", type)
    }

    fun initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this as Activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                mLocationUtils.startLocation()
            }
        } else {
            mLocationUtils.startLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size == 0) {
            return
        }
        when (requestCode) {
            1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationUtils.startLocation()
                }
            }
        }
    }

}