package com.liangzhiyang.youmi

import android.app.Application
import android.content.Context
import android.util.Log
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.orhanobut.hawk.Hawk
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.socialize.PlatformConfig
import kotlin.properties.Delegates





/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

class MyApp : Application() {

    companion object {

        var context: Context by Delegates.notNull()
            private set

        val options = RequestOptions()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Hawk.init(this).build()

        initShare()
        initOptions()

    }

    private fun initOptions() {
        options.placeholder(R.mipmap.home_logo)
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        options.dontAnimate()
    }

    fun initShare() {
        UMConfigure.init(this, "5c7cb99c0cafb2c2f4000939"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE,"7bdaaab2cb0cb94b99ecd66ce8382b78")

        PlatformConfig.setWeixin("wxe70827fe186f27d9", "3baf1193c85774b3fd9d18447d76cab0")
        PlatformConfig.setSinaWeibo("1707621739", "bd0d8caf33f842cae977048478031c72", "http://sns.whalecloud.com");
        PlatformConfig.setQQZone("101559140", "c7394704798a158208a74ab60104f0ba")

        //获取消息推送代理示例
        val mPushAgent = PushAgent.getInstance(this)
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.e("tag","---------------注册成功  token: " + deviceToken)
            }

            override fun onFailure(s: String, s1: String) {
               Log.e("tag","-----------------注册失败： " + s + "  " + s1)
            }
        })

    }



}