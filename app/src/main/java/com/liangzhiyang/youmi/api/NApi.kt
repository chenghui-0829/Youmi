package com.liangzhiyang.youmi.api

import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import io.reactivex.disposables.Disposable

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

object NApi {

    fun getGirl(): Disposable {
        return ApiManager.service.getVerficationCode("22","0")
                .compose(RxJavaHelper.observableToMain())
                .subscribe()
    }

}

