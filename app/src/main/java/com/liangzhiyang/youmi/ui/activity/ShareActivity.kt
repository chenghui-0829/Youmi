package com.liangzhiyang.youmi.ui.activity
import android.app.Activity
import android.content.Intent
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.base.BaseActivity
import com.liangzhiyang.youmi.model.InfoBean
import com.liangzhiyang.youmi.utils.Config
import com.orhanobut.hawk.Hawk
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMWeb
import kotlinx.android.synthetic.main.activity_share.*





class ShareActivity: BaseActivity() {
    var uWeb : UMWeb ?= null

    override fun layoutId(): Int {
        return R.layout.activity_share
    }

    override fun initData() {
        val infoBean = Hawk.get<InfoBean>(Config.USER_INFO, null)
        if (infoBean == null) return

        uWeb = UMWeb(infoBean.accountUrl)
        uWeb?.apply {
            uWeb!!.title = infoBean.accountName
//        uWeb.setThumb(UMImage(this, BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)))
            uWeb!!.description = infoBean.accountSlogan
        }
    }

    override fun initView() {
        share_bg.setOnClickListener { finish() }
        share_cancle.setOnClickListener { finish() }
    }

    override fun loadData() {
    }

    companion object{
        fun startActivity(context:Activity){
            val intent = Intent(context,ShareActivity::class.java)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.alpha_enter,0)
        }
    }

    override fun initListener() {
        super.initListener()

        share_weichat.setOnClickListener { share(SHARE_MEDIA.WEIXIN) }
        share_circle.setOnClickListener { share(SHARE_MEDIA.WEIXIN_CIRCLE) }
        share_qq.setOnClickListener { share(SHARE_MEDIA.QQ) }
        share_qq_space.setOnClickListener { share(SHARE_MEDIA.QZONE) }
        share_sine.setOnClickListener { share(SHARE_MEDIA.SINA) }
//        share_copy.setOnClickListener{
//            CopyUtils.copyText(this,shareUrl)
//        }
        share_cancle.setOnClickListener { finish() }
    }

    fun share(plat: SHARE_MEDIA){
        ShareAction(this)
                .setPlatform(plat)
                .withMedia(uWeb)
                .withText("你好")
                .setCallback(object : UMShareListener{
                    override fun onResult(p0: SHARE_MEDIA?) {
                        dismissProgressDialog()
                        showMessage("分享成功")
                        finish()
                    }

                    override fun onCancel(p0: SHARE_MEDIA?) {
                        dismissProgressDialog()
                        showMessage("分享取消")
                    }

                    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                        dismissProgressDialog()
                        showMessage("分享失败:" + p1.toString())
                        finish()
                    }

                    override fun onStart(p0: SHARE_MEDIA?) {
                        showProgressDialog("")
                    }

                } )
                .share()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,R.anim.alpha_out)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
}