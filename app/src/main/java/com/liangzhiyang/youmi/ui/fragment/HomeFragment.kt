package com.liangzhiyang.youmi.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.base.BaseFragment
import com.liangzhiyang.youmi.model.BannerBean
import com.liangzhiyang.youmi.model.ProductListBean
import com.liangzhiyang.youmi.ui.activity.WebViewActivity
import com.liangzhiyang.youmi.ui.activity.product_list.ProductListActivity
import com.liangzhiyang.youmi.ui.adapter.ProductListAdapter
import com.liangzhiyang.youmi.ui.fragment.contract.IHomeContract
import com.liangzhiyang.youmi.ui.fragment.presenter.HomePresenter
import com.liangzhiyang.youmi.utils.Config
import com.liangzhiyang.youmi.utils.GlideImageLoader
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

class HomeFragment : BaseFragment(), IHomeContract.View {

    private val mPresenter = HomePresenter()
    private val mAdapter: ProductListAdapter by lazy {
        ProductListAdapter(context(),true)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        mPresenter.attachView(this)
        toolbar_back.visibility = View.GONE
        toolbar_title.text = "有米钱袋"
        home_recyclerview.layoutManager = LinearLayoutManager(context())
        home_recyclerview.adapter = mAdapter
        smart_refreshlayout.setEnableLoadMore(false)
    }

    override fun initListener() {
        rmtj_rb.setOnClickListener { ProductListActivity.startActivity(context(), "热门推荐", Config.PRODUCT_TYPE_HOT) }
        zxsx_rb.setOnClickListener { ProductListActivity.startActivity(context(), "最新上线", Config.PRODUCT_TYPE_NEW) }
        xejs_rb.setOnClickListener { ProductListActivity.startActivity(context(), "小额极速", Config.PRODUCT_TYPE_SMALL) }
        deds_rb.setOnClickListener { ProductListActivity.startActivity(context(), "大额低息", Config.PRODUCT_TYPE_BIG) }
        smart_refreshlayout.setOnRefreshListener {
            lazyLoad()
        }
    }

    override fun lazyLoad() {
        mPresenter.getIndexBanner()
        mPresenter.getIndexList()
    }

    override fun setBanner(banner: BannerBean) {
        smart_refreshlayout.finishRefresh()
        home_banner.setImages(banner.bannerList).setImageLoader(GlideImageLoader()).start()
        home_banner.setOnBannerListener {
            position ->
            val bannerUrl = banner.bannerList.get(position).clickUrl
            if (!TextUtils.isEmpty(bannerUrl)){
                val mobile = Hawk.get<String>(Config.USER_PHONE,"")
                mPresenter.dataReport(mobile,"",bannerUrl,"5")
                WebViewActivity.startActivity(context(),bannerUrl,"")
            }
        }
        scroll_tv.setDatas(banner.bannerCastList)
    }

    override fun setListData(data: ProductListBean) {
        smart_refreshlayout.finishRefresh()
        mAdapter.setData(data.list)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}