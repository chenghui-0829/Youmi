package com.liangzhiyang.youmi.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.liangzhiyang.youmi.MyApp
import com.liangzhiyang.youmi.model.BannerBean
import com.youth.banner.loader.ImageLoader

/**
 * Created by zy
 * Date: 2019/2/25
 * desc:
 */

class GlideImageLoader : ImageLoader() {

    override fun displayImage(context: Context, path: Any?, imageView: ImageView) {

        if (path is BannerBean.BannerList) {
            Glide.with(context)
                    .load(path.imgUrl)
                    .apply(MyApp.options)
                    .into(imageView)
        }

    }

}