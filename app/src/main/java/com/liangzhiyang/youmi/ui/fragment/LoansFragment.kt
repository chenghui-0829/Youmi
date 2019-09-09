package com.liangzhiyang.youmi.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.RadioGroup
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.base.BaseFragment
import com.liangzhiyang.youmi.listener.OnFeatureClickListener
import com.liangzhiyang.youmi.model.BannerBean
import com.liangzhiyang.youmi.model.ProductDetailBean
import com.liangzhiyang.youmi.ui.adapter.ProductListAdapter
import com.liangzhiyang.youmi.ui.fragment.contract.ILoansContract
import com.liangzhiyang.youmi.ui.fragment.presenter.LoansPresenter
import com.liangzhiyang.youmi.view.FeaturePopupWindow
import com.liangzhiyang.youmi.view.MultiLineRadioGroup
import com.liangzhiyang.youmi.view.SortPopupWindow
import com.youmi.qiandai.listener.OnSortClickListener
import kotlinx.android.synthetic.main.fragment_loans.*
import kotlinx.android.synthetic.main.layout_base_toolbar.*
import kotlinx.android.synthetic.main.layout_empty.*

/**
 * Created by zy
 * Date: 2019/2/15
 * desc:
 */

class LoansFragment : BaseFragment(), ILoansContract.View, OnSortClickListener, OnFeatureClickListener {

    private val mPresenter = LoansPresenter()
    private val mSortWindow = SortPopupWindow(context(), this)
    private val mFeatureWindow = FeaturePopupWindow(context(), this)
    private var mType = ""
    private var mFeature = ""
    private val mAdapter by lazy { ProductListAdapter(context()) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_loans
    }

    override fun initView() {
        mPresenter.attachView(this)
        toolbar_title.text = "贷款产品"
        toolbar_back.visibility = View.GONE

        loans_rv.setHasFixedSize(true)
        loans_rv.layoutManager = LinearLayoutManager(context())
        loans_rv.adapter = mAdapter
    }

    override fun initListener() {
        sort_layout.setOnClickListener {
            if (mSortWindow.isShowing) {
                mSortWindow.dismiss()
            } else {
                mSortWindow.showAsDropDown(classification_tv)
                classification_tv.setTextColor(resources.getColor(R.color.default_green))
                sort_arrow.setImageResource(R.mipmap.ic_arrow_up)
            }
        }
        feature_layout.setOnClickListener {
            if (mFeatureWindow.isShowing) {
                mFeatureWindow.dismiss()

            } else {
                mFeatureWindow.showAsDropDown(feature_tv)
                feature_tv.setTextColor(resources.getColor(R.color.default_green))
                feature_arrow.setImageResource(R.mipmap.ic_arrow_up)
            }
        }
        loans_srl.setOnLoadMoreListener {
            mPresenter.getList(mType, mFeature, mPresenter.getPager() + 1)
        }
        loans_srl.setOnRefreshListener {
            mPresenter.getBanner()
        }
        mSortWindow.setOnDismissListener {
            classification_tv.setTextColor(resources.getColor(R.color.default_black))
            sort_arrow.setImageResource(R.mipmap.ic_arrow_down)
        }
        mFeatureWindow.setOnDismissListener {
            feature_tv.setTextColor(resources.getColor(R.color.default_black))
            feature_arrow.setImageResource(R.mipmap.ic_arrow_down)
        }
    }

    override fun lazyLoad() {
        mPresenter.getList("", "", 1)
        mPresenter.getBanner()
    }

    override fun setReceive(bannerBean: BannerBean) {
        loans_srl.finishRefresh()
        scroll_tv.setDatas(bannerBean.bannerCastList)
    }

    override fun setAdapterItems(data: List<ProductDetailBean>) {
        mAdapter.setData(data)
        if (data == null || data.size == 0) {
            empty_layout.visibility = View.VISIBLE
            loans_srl.visibility = View.GONE
        } else {
            empty_layout.visibility = View.GONE
            loans_srl.visibility = View.VISIBLE
        }
    }

    override fun addAdapterItems(data: List<ProductDetailBean>) {
        loans_srl.finishLoadMore()
        mAdapter.addData(data)
    }

    override fun setFinishLoadMore() {
        loans_srl.finishLoadMoreWithNoMoreData()
    }

    var radioGroup: RadioGroup ?= null
    var multiLineRadioGroup: MultiLineRadioGroup ?= null

    override fun onSortClick(sort: String,group: RadioGroup) {
        mType = sort
        mFeature = ""
        radioGroup = group
        multiLineRadioGroup?.clearChecked()
        classification_tv.postDelayed(Runnable {
            mSortWindow.dismiss()
            loans_srl.setNoMoreData(false)
            mPresenter.getList(mType, mFeature, 1)
        }, 200)
    }

    override fun onFeatureClick(group: MultiLineRadioGroup, position: Int, checked: Boolean) {
        mFeature = (position+1).toString()
        mType =""
        multiLineRadioGroup = group
        radioGroup?.clearCheck()
        feature_tv.postDelayed(Runnable {
            mFeatureWindow.dismiss()
            loans_srl.setNoMoreData(false)
            mPresenter.getList(mType, mFeature, 1)
        }, 200)

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}