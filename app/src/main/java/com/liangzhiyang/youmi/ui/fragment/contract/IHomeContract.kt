package com.liangzhiyang.youmi.ui.fragment.contract

import com.liangzhiyang.youmi.base.IBasePresenter
import com.liangzhiyang.youmi.base.IBaseView
import com.liangzhiyang.youmi.model.BannerBean
import com.liangzhiyang.youmi.model.ProductListBean

/**
 * Created by zy
 * Date: 2019/2/24
 * desc:
 */

interface IHomeContract {

    interface View : IBaseView {

        fun setBanner(banner: BannerBean)
        fun setListData(data: ProductListBean)
    }

    interface Presenter: IBasePresenter<View> {
        fun getIndexBanner()
        fun getIndexList()
        fun dataReport(mobile:String,productId:String,bannerUrl:String,type:String)
    }

}