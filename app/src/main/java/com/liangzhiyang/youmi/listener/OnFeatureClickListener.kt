package com.liangzhiyang.youmi.listener

import com.liangzhiyang.youmi.view.MultiLineRadioGroup

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

interface OnFeatureClickListener {

    fun onFeatureClick(group: MultiLineRadioGroup, position: Int,
                       checked : Boolean)

}