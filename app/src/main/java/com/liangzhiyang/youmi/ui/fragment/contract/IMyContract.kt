package com.liangzhiyang.youmi.ui.fragment.contract

import com.liangzhiyang.youmi.base.IBasePresenter
import com.liangzhiyang.youmi.base.IBaseView


/**
 * Created by zy
 * Date: 2019/2/24
 * desc:
 */
 
interface IMyContract{

    interface View : IBaseView {

    }

    interface Presenter: IBasePresenter<View> {

    }

}