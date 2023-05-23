package com.github.xs93.framework.img.coil

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

/**
 * coil databinding 扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 10:46
 * @email 466911254@qq.com
 */

@BindingAdapter(*["coilUrl", "coilPlaceholder", "coilError"], requireAll = false)
fun loadImage(view: ImageView, url: String? = null, placeholder: Drawable? = null, error: Drawable? = null) {
    val loadUrl = url ?: ""
    view.load(loadUrl) {
        if (placeholder != null) {
            this.placeholder(placeholder)
        }
        if (error != null) {
            error(error)
        }
    }
}