package com.github.xs93.wanandroid.common.ktx

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/7/22 14:57
 * @email 466911254@qq.com
 */

/**
 * coil加载图片
 * @param view ImageView
 * @param url String?
 * @param default Drawable?
 */
@BindingAdapter(*["app:url", "app:default"], requireAll = false)
fun loadCircleCropImage(view: ImageView, url: String? = null, default: Drawable? = null) {
    view.load(url) {
        placeholder(default)
        error(default)
    }
}

