package com.youmi.qiandai.ui.activity.product_list

import com.liangzhiyang.youmi.base.IBasePresenter
import com.liangzhiyang.youmi.base.IBaseView
import com.liangzhiyang.youmi.model.ProductDetailBean

/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */

interface IProductListContract {

    interface View : IBaseView {
        fun setAdapterItems(data: List<ProductDetailBean>)

        fun addAdapterItems(data: List<ProductDetailBean>)

        fun setFinishLoadMore()
    }

    interface Presenter : IBasePresenter<View> {
        fun getProductList(type: String, productFeature: String, currPage: Int, pageSize: Int)
    }

}