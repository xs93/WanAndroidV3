package com.github.xs93.utils.ktx

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Spanned
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Fragment 扩展工具
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 14:19
 * @email 466911254@qq.com
 */


/**
 * Fragment的 View的Lifecycle 的生命周期对象
 */
val Fragment.viewLifecycle
    get() = viewLifecycleOwner.lifecycle

@ColorInt
fun Fragment.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(requireContext(), id)
}

fun Fragment.colorStateList(@ColorRes id: Int): ColorStateList? {
    return ContextCompat.getColorStateList(requireContext(), id)
}

fun Fragment.drawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(requireContext(), id)
}

fun Fragment.string(@StringRes id: Int, vararg formatArgs: Any?): String {
    return getString(id, *formatArgs)
}

fun Fragment.htmlString(@StringRes resId: Int): Spanned {
    return getString(resId).toHtml()
}

fun Fragment.quantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?): String {
    return resources.getQuantityString(id, quantity, *formatArgs)
}