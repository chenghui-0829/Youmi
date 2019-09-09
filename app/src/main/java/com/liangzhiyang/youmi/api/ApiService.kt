package com.liangzhiyang.youmi.api

import com.liangzhiyang.youmi.model.*
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

interface ApiService {

    //发送验证码
    @FormUrlEncoded
    @POST("user/getVerificationCode.json")
    fun getVerficationCode(@Field("mobile") mobile: String, @Field("type") type: String): Observable<BaseResult<BaseData>>

    //登录
    @FormUrlEncoded
    @POST("user/login.json")
    fun login(@Field("mobile") mobile: String, @Field("verificationCode") code: String, @Field("address")address:String): Observable<BaseResult<LoginBean>>

    //产品列表
    @FormUrlEncoded
    @POST("product/list.json")
    fun getProductList(@Field("type") type: String, @Field("productFeature") productFeature: String,
                       @Field("currPage") currPage: Int, @Field("pageSize") pageSize: Int,
                       @Field("isIndex") isIndex: Int): Observable<BaseResult<ProductListBean>>

    //产品详情
    @FormUrlEncoded
    @POST("product/info.json")
    fun getProductDetail(@Field("productId") productId: String): Observable<BaseResult<ProductDetailBean>>

    //首页banner 1首页 2贷款页
    @FormUrlEncoded
    @POST("index/data.json")
    fun getIndexBanner(@Field("broadcastType") broadcastType: String): Observable<BaseResult<BannerBean>>

    //意见反馈
    @FormUrlEncoded
    @POST("user/feedback.json")
    fun feedback(@Field("mobile") mobile: String, @Field("content") content: String): Observable<BaseResult<BaseData>>

    //检查更新
    @FormUrlEncoded
    @POST("version/getVersion.json")
    fun checkUpdate(@Field("channel") channel: String, @Field("currVersion") currVersion: String): Observable<BaseResult<CheckBean>>

    //数据上报
    /**
     * type：点击类型（0：登录按钮 1：获取验证码按钮
     * 2：手机号输入框内输入满11位数字
     * 3：验证码输入框输入满4位数字
     * 4：点击对应产品的注册链接
     * 5:点击banner图片
     * 6:点击新手指南
     * 7:点击邀请好友
     * 8:点击客服（微信）
     *
     * 渠道（1：安卓 2：苹果）
     */
    @FormUrlEncoded
    @POST("statistic/dataReport.json")
    fun dataReport(@Field("channel")channel:String, @Field("mobile")mobile: String,
                   @Field("productId")productId: String,@Field("bannerUrl")bannerUrl: String,
                   @Field("type")type: String): Observable<BaseResult<BaseData>>

    @POST("account/getInfo.json")
    fun getInfo(): Observable<BaseResult<InfoBean>>


}