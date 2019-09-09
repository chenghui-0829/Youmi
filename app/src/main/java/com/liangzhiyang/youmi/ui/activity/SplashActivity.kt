package com.liangzhiyang.youmi.ui.activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.ui.MainActivity
import com.liangzhiyang.youmi.utils.Config
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        val guide = Hawk.get<Boolean>(Config.GUIDE, false)
        val userSign = Hawk.get<String>(Config.USER_SIGN, "")
        appname_tv.postDelayed(Runnable {
            if (guide) {
                if (userSign.isEmpty()) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            } else {
                startActivity(Intent(this, GuideActivity::class.java))
            }
            finish()
        }, 1500)
    }

}