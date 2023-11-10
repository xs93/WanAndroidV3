package com.github.xs93.utils.ktx

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
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

fun Fragment.dp(value: Float): Int {
    val scale = resources.displayMetrics.density
    return (value * scale + 0.5f).toInt()
}

fun Fragment.dp(value: Int): Int {
    val scale = resources.displayMetrics.density
    return (value * scale + 0.5f).toInt()
}

fun Fragment.toDp(value: Float): Float {
    val scale = resources.displayMetrics.density
    return value / scale
}

fun Fragment.toDp(value: Int): Float {
    val scale = resources.displayMetrics.density
    return value / scale
}

fun Fragment.sp(value: Float): Int {
    val convertValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)
    return (convertValue + 0.5f).toInt()
}

fun Fragment.sp(value: Int): Int {
    val convertValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value.toFloat(), resources.displayMetrics)
    return (convertValue + 0.5f).toInt()
}

fun Fragment.toSp(value: Float): Float {
    val scaledDensity = resources.displayMetrics.scaledDensity
    return value / scaledDensity
}

fun Fragment.toSp(value: Int): Float {
    val scaledDensity = resources.displayMetrics.scaledDensity
    return value / scaledDensity
}

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