package com.almightyai.robot.coil.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

/**
 * Coil 加载图片扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/1/22 9:17
 * @email 466911254@qq.com
 */

@BindingAdapter("coilUrl", "coilFallback", "coilPlaceholder", "coilError", requireAll = false)
fun bindLoadImage(
    imageView: ImageView,
    url: String? = null,
    fallback: Drawable? = null,
    placeholder: Drawable? = null,
    error: Drawable? = null
) {
    imageView.load(url) {
        if (fallback != null) {
            fallback(fallback)
        } else {
            if (error != null) {
                fallback(error)
            } else {
                if (placeholder != null) {
                    fallback(placeholder)
                }
            }
        }

        if (placeholder != null) {
            this.placeholder(placeholder)
        }

        if (error != null) {
            error(error)
        }
    }
}