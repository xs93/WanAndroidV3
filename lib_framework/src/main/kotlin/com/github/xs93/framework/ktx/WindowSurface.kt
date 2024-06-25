package com.github.xs93.framework.ktx

import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.xs93.framework.ui.ContentPadding

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

fun View.setOnInsertsChangedListener(listener: (ContentPadding) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insCompat ->
        val insets = insCompat.getInsets(WindowInsetsCompat.Type.systemBars())
        val rContentPadding = ContentPadding(insets.left, insets.top, insets.right, insets.bottom)
        listener(rContentPadding)
        insCompat
    }
    requestApplyInsetsWhenAttached()
}