package com.liangzhiyang.youmi.api.rxjava

import com.liangzhiyang.youmi.model.BaseResult
import io.reactivex.functions.Function

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */
 
class HttpResultFun<T> : Function<BaseResult<T>, T>{

    override fun apply(t: BaseResult<T>): T {

        var state = t.code
        if (state == 0){
            throw Exception("错误")
        }else{
            return t.data
        }

    }

}