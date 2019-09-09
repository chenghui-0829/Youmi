package com.liangzhiyang.youmi.ui.activity.product_detail
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BasePresenter


/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */

class ProductDetailPresenter : BasePresenter<IProductDetailContract.View>(), IProductDetailContract.Presenter {

    override fun getData(productId: String) {
        mView?.showProgressDialog(getContext().getString(R.string.loading_msg))
        ApiManager.service.getProductDetail(productId)
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    mView?.dismissProgressDialog()
                    if (it.isSuccess()) {
                        mView?.setData(it.data)
                    } else {
                        mView?.showMessage(it.desc)
                    }
                }, {
                    mView?.dismissProgressDialog()
                    mView?.showMessage(getContext().getString(R.string.net_error))
                })
    }

}