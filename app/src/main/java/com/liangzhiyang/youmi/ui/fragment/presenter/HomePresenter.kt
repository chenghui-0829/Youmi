package com.liangzhiyang.youmi.ui.fragment.presenter

import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BasePresenter
import com.liangzhiyang.youmi.ui.fragment.contract.IHomeContract
import com.liangzhiyang.youmi.utils.Config


/**
 * Created by zy
 * Date: 2019/2/24
 * desc:
 */

class HomePresenter : BasePresenter<IHomeContract.View>(),IHomeContract.Presenter {
    override fun getIndexBanner() {
        mView?.showProgressDialog(getContext().getString(R.string.loading_msg))
        val disposable = ApiManager.service.getIndexBanner("1")
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    mView?.dismissProgressDialog()
                    if (it.isSuccess()){
                        mView?.setBanner(it.data)
                    }else{
                        mView?.showMessage(it.desc)
                    }
                },{
                    mView?.dismissProgressDialog()
                    mView?.showMessage(getContext().getString(R.string.net_error))
                })
        addSubscription(disposable)
    }

    override fun getIndexList() {
        val disposable = ApiManager.service.getProductList(Config.PRODUCT_TYPE_HOT,"",1,Config.PAGE_SIZE,1)
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    if (it.isSuccess())
                        mView?.setListData(it.data)
                    else
                        mView?.showMessage(it.desc)
                },{
                    mView?.dismissProgressDialog()
                    mView?.showMessage(getContext().getString(R.string.net_error))
                })
        addSubscription(disposable)
    }

    //数据上报
    override fun dataReport(mobile:String,productId:String,bannerUrl:String,type:String){
        val disposable = ApiManager.service.dataReport("1",mobile,productId,bannerUrl,type)
                .compose(RxJavaHelper.observableToMain())
                .subscribe {  }
        addSubscription(disposable)
    }
}