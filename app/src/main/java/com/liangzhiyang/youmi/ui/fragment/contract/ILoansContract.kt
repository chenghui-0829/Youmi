package com.liangzhiyang.youmi.ui.fragment.contract

import com.liangzhiyang.youmi.base.IBasePresenter
import com.liangzhiyang.youmi.base.IBaseView
import com.liangzhiyang.youmi.model.BannerBean
import com.liangzhiyang.youmi.model.ProductDetailBean

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

interface ILoansContract {

    interface View: IBaseView {

        fun setAdapterItems(data: List<ProductDetailBean>)

        fun addAdapterItems(data: List<ProductDetailBean>)

        fun setFinishLoadMore()

        fun setReceive(bannerBean: BannerBean)
    }

    interface Presenter: IBasePresenter<View> {
        fun getList(type: String,feature: String,currPage: Int)
        fun getBanner()
    }


}