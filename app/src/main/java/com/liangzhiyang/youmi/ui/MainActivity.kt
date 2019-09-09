package com.liangzhiyang.youmi.ui

import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.base.BaseActivity
import com.liangzhiyang.youmi.model.FinishEvent
import com.liangzhiyang.youmi.model.SwichEvent
import com.liangzhiyang.youmi.ui.fragment.HomeFragment
import com.liangzhiyang.youmi.ui.fragment.LoansFragment
import com.liangzhiyang.youmi.ui.fragment.MyFragment
import com.liangzhiyang.youmi.utils.ActivityManager
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {

    private var mHomeFragment: HomeFragment? = null
    private var mLoansFragment: LoansFragment? = null
    private var mMyFragment: MyFragment? = null

    private var mIndex = -1

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        EventBus.getDefault().register(this)
    }

    override fun initView() {
        swichFragment(0)
    }

    private fun swichFragment(index: Int) {
        if (mIndex == index) return
        val transaction = supportFragmentManager.beginTransaction()
        hideFragment(transaction)
        when (index) {
            0//首页
            -> mHomeFragment?.let { transaction.show(it) } ?: HomeFragment().let {
                mHomeFragment = it
                transaction.add(R.id.main_container, it, "home")
            }
            1//贷款
            -> mLoansFragment?.let { transaction.show(it) } ?: LoansFragment().let {
                mLoansFragment = it
                transaction.add(R.id.main_container, it, "loans")
            }
            2//我的
            -> mMyFragment?.let { transaction.show(it) } ?: MyFragment().let {
                mMyFragment = it
                transaction.add(R.id.main_container, it, "my")
            }

            else -> {

            }
        }
        transaction.commitAllowingStateLoss()
        mIndex = index

    }

    override fun initListener() {
        radio_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.home_rb -> swichFragment(0)
                R.id.loans_rb -> swichFragment(1)
                R.id.my_rb -> swichFragment(2)
            }
        }
    }

    override fun loadData() {
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mLoansFragment?.let { transaction.hide(it) }
        mMyFragment?.let { transaction.hide(it) }
    }

    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                ActivityManager.getInstance().AppExit(this)
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showMessage("再按一次退出程序")
            }
        }
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun finishEvent(event: FinishEvent){
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun swichEvent(swichEvent: SwichEvent){
        swichFragment(swichEvent.index)
        radio_group.clearCheck()
        radio_group.check(R.id.loans_rb)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
