package com.youmi.qiandai.ui.activity.product_list

import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BasePresenter

/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */
 
class ProductListPresenter : BasePresenter<IProductListContract.View>(), IProductListContract.Presenter{

    private var mPager = 1
    private var mMaxPager = 1

    override fun getProductList(type: String, productFeature: String, currPage: Int, pageSize: Int) {
        mPager = currPage
        mView?.showProgressDialog(getContext().resources.getString(R.string.loading_msg))
        val disposable = ApiManager.service.getProductList(type,productFeature,currPage,pageSize,0)
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    mView?.dismissProgressDialog()
                    if (it.isSuccess()){
                        mMaxPager = Math.ceil(it.data.totalCount/pageSize.toDouble()).toInt()
                        if (mPager >= mMaxPager)mView?.setFinishLoadMore()
                        if (mPager == 1){
                            mView?.setAdapterItems(it.data.list)
                        }else{
                            mView?.addAdapterItems(it.data.list)
                        }
                    }
                },{
                    mView?.dismissProgressDialog()
                    mView?.showMessage(getContext().getString(R.string.net_error))
                })
        addSubscription(disposable = disposable)
    }

    fun getPager(): Int{
        return mPager
    }

}