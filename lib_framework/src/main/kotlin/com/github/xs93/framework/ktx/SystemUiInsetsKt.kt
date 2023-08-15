package com.github.xs93.framework.ktx

import android.content.Context
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.xs93.framework.ui.Insets
import com.github.xs93.utils.ktx.dp
import com.github.xs93.utils.ktx.getAppMateData

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 16:02
 * @email 466911254@qq.com
 */

fun Insets.landscape(context: Context): Insets {
    val displayMetrics = context.resources.displayMetrics

    val landscapeMinWidthDp = context.getAppMateData().getInt("surface_landscape_min_width_dp")
    val minWidth = landscapeMinWidthDp.dp(context)

    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels

    return if (width > height && width > minWidth) {
        val expectedWidth = width.coerceAtMost(height.coerceAtLeast(minWidth))
        val padding = (width - expectedWidth).coerceAtLeast(start + end) / 2
        copy(start = padding.coerceAtLeast(start), end = padding.coerceAtLeast(end))
    } else {
        this
    }
}

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

fun View.setOnInsertsChangedListener(adaptLandscape: Boolean = true, listener: (Insets) -> Unit) {
    setOnApplyWindowInsetsListener { v, ins ->
        val compat = WindowInsetsCompat.toWindowInsetsCompat(ins)
        val insets = compat.getInsets(WindowInsetsCompat.Type.systemBars())

        val rInsets = if (ViewCompat.getLayoutDirection(v) == ViewCompat.LAYOUT_DIRECTION_LTR) {
            Insets(insets.left, insets.top, insets.right, insets.bottom)
        } else {
            Insets(insets.right, insets.top, insets.left, insets.bottom)
        }
        listener(if (adaptLandscape) rInsets.landscape(v.context) else rInsets)
        v.onApplyWindowInsets(compat.toWindowInsets())
    }
    requestApplyInsetsWhenAttached()
}