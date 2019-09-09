package com.liangzhiyang.youmi.ui.fragment.presenter

import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BasePresenter
import com.liangzhiyang.youmi.ui.fragment.contract.ILoansContract
import com.liangzhiyang.youmi.utils.Config

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class LoansPresenter : BasePresenter<ILoansContract.View>(), ILoansContract.Presenter {
    override fun getBanner() {
        val dis = ApiManager.service.getIndexBanner("2")
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    mView?.dismissProgressDialog()
                    if (it.isSuccess()){
                        mView?.setReceive(it.data)
                    }else{
                        mView?.showMessage(it.desc)
                    }
                },{
                    mView?.dismissProgressDialog()
                    mView?.showMessage(getContext().getString(R.string.net_error))
                })
        addSubscription(dis)
    }

    private var mPager = 1
    private var mMaxPager = 1

    override fun getList(type: String, feature: String, currPage: Int) {
        mPager = currPage
        mView?.showProgressDialog(getContext().resources.getString(R.string.loading_msg))
        val disposable = ApiManager.service.getProductList(type,feature,currPage,Config.PAGE_SIZE,0)
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    mView?.dismissProgressDialog()
                    if (it.isSuccess()){
                        mPager == currPage
                        mMaxPager = Math.ceil(it.data.totalCount/ Config.PAGE_SIZE.toDouble()).toInt()
                        if (mPager >= mMaxPager)mView?.setFinishLoadMore()
                        if (mPager == 1){
                            mView?.setAdapterItems(it.data.list)
                        }else{
                            mView?.addAdapterItems(it.data.list)
                        }
                    }else{
                        mView!!.showMessage(it.desc)
                    }
                },{
                    mView?.dismissProgressDialog()
                    mView?.showMessage(getContext().getString(R.string.net_error))
                })
        addSubscription(disposable)
    }

    fun getPager(): Int{
        return mPager
    }

}