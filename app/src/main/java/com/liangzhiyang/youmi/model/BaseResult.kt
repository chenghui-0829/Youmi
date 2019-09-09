package com.liangzhiyang.youmi.model


/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */
 
class BaseResult<T>(val code: Int,var data: T,var desc: String){

    fun isSuccess() : Boolean{
        return code == 1
    }

}