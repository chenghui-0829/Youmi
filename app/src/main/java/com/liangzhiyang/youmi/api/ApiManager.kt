package com.liangzhiyang.youmi.api

import android.util.Log
import com.liangzhiyang.youmi.MyApp
import com.liangzhiyang.youmi.utils.Config
import com.liangzhiyang.youmi.utils.NetworkUtil
import com.orhanobut.hawk.Hawk
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

object ApiManager {

    val BASE_URL = "http://47.97.161.209:8000/"
    var userSign = ""

    //lazy延迟初始化，用于常量val，多用于单例模式
    val service: ApiService by lazy {
        userSign = Hawk.get<String>(Config.USER_SIGN,"")
        Log.e("tag","==============  userSign:" + userSign)
        getRetrofit().create(ApiService::class.java)
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getOkHttpClient(): OkHttpClient{
        //添加log拦截器
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        //设置缓存大小
        val cacheFile = File(MyApp.context.cacheDir,"youmi_cache")
        var cache = Cache(cacheFile,1024 * 1024 * 50)

        return OkHttpClient.Builder()
                .addInterceptor(addQueryParameterInterceptor())//添加参数
                .addInterceptor(addHeaderInterceptor())//添加token
                .addInterceptor(httpLoggingInterceptor)//添加日志拦截
                .cache(cache)
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .build()
    }

    /**
     * 设置公用头
     */
    private fun addHeaderInterceptor(): Interceptor{
        return Interceptor {
            chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    //自定义头
//                    .header("token","")
                    .method(originalRequest.method(),originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor{
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                    //自定义请求参数
                    .addQueryParameter("userSign", "kdZCQhp+be2Z8m7zkdyoi7hibfZ5H9ic") //"kdZCQhp+be2Z8m7zkdyoi7hibfZ5H9ic"
                    .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置缓存
     */
    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable(MyApp.context)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            if (NetworkUtil.isNetworkAvailable(MyApp.context)) {
                val maxAge = 0
                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build()
            } else {
                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("nyn")
                        .build()
            }
            response
        }
    }

}