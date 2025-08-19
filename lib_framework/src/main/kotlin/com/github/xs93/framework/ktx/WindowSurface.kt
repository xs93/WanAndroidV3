package com.github.xs93.framework.ktx

import android.view.View
import android.view.View.OnAttachStateChangeListener

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 16:02
 * @email 466911254@qq.com
 */

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}