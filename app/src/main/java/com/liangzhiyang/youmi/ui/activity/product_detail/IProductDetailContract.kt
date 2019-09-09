package com.liangzhiyang.youmi.ui.activity.product_detail
import com.liangzhiyang.youmi.base.IBasePresenter
import com.liangzhiyang.youmi.base.IBaseView
import com.liangzhiyang.youmi.model.ProductDetailBean


/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */

interface IProductDetailContract {

    interface View : IBaseView {
        fun setData(productDetailBean: ProductDetailBean)
    }

    interface Presenter : IBasePresenter<View> {
        fun getData(productId: String)
    }

}