package com.liangzhiyang.youmi.ui.activity.product_list

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.base.BaseActivity
import com.liangzhiyang.youmi.model.ProductDetailBean
import com.liangzhiyang.youmi.ui.adapter.ProductListAdapter
import com.liangzhiyang.youmi.utils.Config
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.youmi.qiandai.ui.activity.product_list.IProductListContract
import com.youmi.qiandai.ui.activity.product_list.ProductListPresenter
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*

/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */

class ProductListActivity : BaseActivity(), IProductListContract.View, OnLoadMoreListener {

    companion object {
        fun startActivity(activity: Context, title: String, type: String) {
            val intent = Intent(activity,ProductListActivity::class.java)
            intent.putExtra("TITLE", title)
            intent.putExtra("TYPE", type)
            activity.startActivity(intent)
        }
    }

    private val mPresenter by lazy { ProductListPresenter() }
    private val mAdapter by lazy { ProductListAdapter(context = this) }
    private var title = ""
    private var type = ""

    override fun layoutId(): Int {
        return R.layout.activity_product_list
    }

    override fun initData() {
        title = intent.getStringExtra("TITLE")
        type = intent.getStringExtra("TYPE")
    }

    override fun initView() {
        mPresenter.attachView(this)
        toolbar_title.text = title
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = mAdapter
        smart_refreshlayout.setEnableRefresh(false)
        smart_refreshlayout.setOnLoadMoreListener(this)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mPresenter.getProductList(type,"",mPresenter.getPager()+1,Config.PAGE_SIZE)
    }

    override fun setAdapterItems(data: List<ProductDetailBean>) {
        mAdapter.setData(data)
    }

    override fun addAdapterItems(data: List<ProductDetailBean>) {
        smart_refreshlayout.finishLoadMore()
        mAdapter.addData(data)
    }

    override fun setFinishLoadMore() {
        smart_refreshlayout.finishLoadMoreWithNoMoreData()
    }

    override fun loadData() {
        mPresenter.getProductList(type, "", mPresenter.getPager(), Config.PAGE_SIZE)
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}