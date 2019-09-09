package com.liangzhiyang.youmi.model

/**
 * Created by zy
 * Date: 2019/2/24
 * desc:
 */

data class BannerBean(val bannerCastList: List<String>,val bannerList: List<BannerList>){


    data class BannerList(val clickUrl: String,val imgUrl: String)

}