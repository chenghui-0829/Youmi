package com.liangzhiyang.youmi.api.rxjava

import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

object RxJavaHelper {

    /**
     * 切换到主线程
     */
    fun <T> observeOnMainThread(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    @JvmStatic
    fun <T> observableToMain(): ObservableTransformer<T, T> {

        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    @JvmStatic
    fun <T> flowableToMain(): FlowableTransformer<T, T> {

        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

}