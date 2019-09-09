package com.liangzhiyang.youmi.ui.activity.login

import com.liangzhiyang.youmi.base.IBasePresenter
import com.liangzhiyang.youmi.base.IBaseView
import com.liangzhiyang.youmi.model.LoginBean

/**
 * Created by zy
 * Date: 2019/2/18
 * desc:
 */

interface ILoginContract {

    interface View : IBaseView {
        /**
         * 发送成功
         */
        fun sendSucceed()

        /**
         * 计时
         */
        fun timer(desc: String)

        /**
         * 重新发送
         */
        fun resendCode()

        fun loginSuccess(loginBean: LoginBean, phone: String)

        /**
         * 数据上报
         */
        fun reportType(type:String)
    }

    interface Presenter : IBasePresenter<View> {
        fun sendCode(phone: String)
        fun login(phone: String, code: String, city: String)
        fun dataReport(mobile:String,productId:String,bannerUrl:String,type:String)
    }

}