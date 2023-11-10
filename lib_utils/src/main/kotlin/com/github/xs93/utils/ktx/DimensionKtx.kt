package com.github.xs93.utils.ktx

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment

/**
 * 尺寸单位换算
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/11/10 14:30
 * @email 466911254@qq.com
 */

// region dp 和 sp 与px单位换算
fun Context.dp(value: Float): Int {
    val convertValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
    return (convertValue + 0.5f).toInt()
}

fun Context.toDp(value: Float): Float {
    val scale = resources.displayMetrics.density
    return value / scale
}

fun Context.sp(value: Float): Int {
    val convertValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)
    return (convertValue + 0.5f).toInt()
}

fun Context.toSp(value: Float): Float {
    val scaledDensity = resources.displayMetrics.scaledDensity
    return value / scaledDensity
}

fun Fragment.dp(value: Float): Int {
    return requireContext().dp(value)
}

fun Fragment.toDp(value: Float): Float {
    return requireContext().toDp(value)
}

fun Fragment.sp(value: Float): Int {
    return requireContext().sp(value)
}

fun Fragment.toSp(value: Float): Float {
    return requireContext().toSp(value)
}
// endregion

// region mm和px转换
fun Context.mm(value: Float): Int {
    val convertValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value, resources.displayMetrics)
    return (convertValue + 0.5f).toInt()
}

fun Context.toMm(value: Float): Float {
    val xdpi = resources.displayMetrics.xdpi
    return value * 25.4f / xdpi
}

fun Fragment.mm(value: Float): Int {
    return requireContext().mm(value)
}

fun Fragment.toMm(value: Float): Float {
    return requireContext().toMm(value)
}
// endregion