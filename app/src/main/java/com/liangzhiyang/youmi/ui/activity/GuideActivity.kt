package com.liangzhiyang.youmi.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.ui.MainActivity
import com.liangzhiyang.youmi.ui.activity.login.LoginActivity
import com.liangzhiyang.youmi.utils.Config
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_guide.*

/**
 * Created by zy
 * Date: 2019/2/25
 * desc:
 */

class GuideActivity : AppCompatActivity(),View.OnClickListener {
    override fun onClick(v: View?) {
        jump()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        viewpager.adapter = MyPagerAdapter(this,this)
        viewpager.offscreenPageLimit = 3
        guide_jump_iv.setOnClickListener { jump() }
    }


    class MyPagerAdapter(val context: Context,val onClickListener: View.OnClickListener) : PagerAdapter() {

        override fun isViewFromObject(p0: View, p1: Any): Boolean {
            return p0 == p1
        }

        override fun getCount(): Int {
            return 3
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(context).inflate(R.layout.item_pager, null, false)
            val imageIv = view.findViewById<ImageView>(R.id.guide_icon_iv)
            val descTv = view.findViewById<TextView>(R.id.guide_desc_tv)

            val startIv = view.findViewById<ImageView>(R.id.guide_start_iv)
            startIv.setOnClickListener {
                onClickListener.onClick(view)
            }

            startIv.visibility = View.GONE
            when (position) {
                0 -> {
                    descTv.text = "额度高  利息低"
                    imageIv.setImageResource(R.mipmap.img_guide_page1)
                }
                1 -> {
                    descTv.text = "轻松刷脸  就能贷款"
                    imageIv.setImageResource(R.mipmap.img_guide_page2)
                }
                2 -> {
                    descTv.text = "当天申请  当天放贷"
                    imageIv.setImageResource(R.mipmap.img_guide_page2)
                    startIv.visibility = View.VISIBLE
                }
                else -> descTv.text = "额度高  利息低"

            }
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (`object` is View)
                container.removeView(`object`)
        }

    }

    fun jump() {
        Hawk.put(Config.GUIDE, true)
        val userSign = Hawk.get<String>(Config.USER_SIGN, "")
        if (userSign.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            GuideActivity::finish
        }
    }
}