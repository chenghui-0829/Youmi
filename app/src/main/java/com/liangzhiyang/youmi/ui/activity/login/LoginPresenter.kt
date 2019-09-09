package com.liangzhiyang.youmi.ui.activity.login

import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BasePresenter
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by zy
 * Date: 2019/2/18
 * desc:
 */

class LoginPresenter : BasePresenter<ILoginContract.View>(), ILoginContract.Presenter {


    override fun sendCode(phone: String) {
        if (checkPhone(phone)){
            mView?.reportType("1")
            val disposable = ApiManager.service.getVerficationCode(phone,"0")
                    .compose(RxJavaHelper.observableToMain())
                    .subscribe({
                        if (it.isSuccess()) {
                            mView?.apply {
                                startTimer()
                            }
                        }
                    }, {

                    })
            addSubscription(disposable = disposable)
        }
    }

    override fun login(phone: String, code: String, cyty: String) {
        if (!checkPhone(phone))return
        if (code.isEmpty()){
            mView?.showMessage("请输入验证码")
            return
        }
        mView?.reportType("0")
        mView?.showProgressDialog("正在登录...")
        val disposable = ApiManager.service.login(phone,code,cyty)
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    mView?.dismissProgressDialog()
                    if (it.isSuccess()){
                        mView?.loginSuccess(it.data,phone)
                    }
                },{
                    mView?.dismissProgressDialog()
                })
        addSubscription(disposable)
    }

    fun startTimer() {
        val count = 60L
        mView?.sendSucceed()
        val disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map { count - it }
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    mView?.apply {
                        mView?.timer(it.toString() + " s")
                    }
                }, {

                }, {
                    mView?.resendCode()
                })
        addSubscription(disposable)
    }

    fun checkPhone(phone:String): Boolean{
        if (phone.isNullOrEmpty()){
            mView?.showMessage("请输入手机号")
            return false
        } else if (phone.length < 11) {
            mView?.showMessage("请输入正确的手机号")
            return false
        } else{
            return true
        }
    }

    //数据上报
    override fun dataReport(mobile:String,productId:String,bannerUrl:String,type:String){
        val disposable = ApiManager.service.dataReport("1",mobile,productId,bannerUrl,type)
                .compose(RxJavaHelper.observableToMain())
                .subscribe {  }
        addSubscription(disposable)
    }

}