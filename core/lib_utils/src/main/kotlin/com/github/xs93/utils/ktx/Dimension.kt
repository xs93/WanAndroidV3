package com.github.xs93.utils.ktx

import android.content.Context
import android.os.Build
import android.util.TypedValue
import androidx.fragment.app.Fragment
import com.github.xs93.utils.AppInject

/**
 * 尺寸单位换算
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/11/10 14:30
 * @email 466911254@qq.com
 */

// region dp 和 sp 与px单位换算

fun Float.dp(context: Context? = null): Int {
    val resources = context?.resources ?: AppInject.getApp().resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)
        .toInt()
}

fun Float.toDp(context: Context? = null): Float {
    val resources = context?.resources ?: AppInject.getApp().resources
    val convertValue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)
    } else {
        val density = resources.displayMetrics.density
        this / density
    }
    return convertValue
}

fun Float.sp(context: Context? = null): Int {
    val resources = context?.resources ?: AppInject.getApp().resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, resources.displayMetrics)
        .toInt()
}

fun Float.toSp(context: Context? = null): Float {
    val resources = context?.resources ?: AppInject.getApp().resources
    val convertValue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_SP, this, resources.displayMetrics)
    } else {
        @Suppress("DEPRECATION")
        val scaledDensity = resources.displayMetrics.scaledDensity
        this / scaledDensity
    }
    return convertValue
}

fun Float.mm(context: Context? = null): Int {
    val resources = context?.resources ?: AppInject.getApp().resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, this, resources.displayMetrics)
        .toInt()
}

fun Float.toMM(context: Context? = null): Float {
    val resources = context?.resources ?: AppInject.getApp().resources
    val convertValue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_MM, this, resources.displayMetrics)
    } else {
        val xdpi = resources.displayMetrics.xdpi
        if (xdpi == 0f) {
            0f
        } else {
            this * 25.4f / xdpi
        }
    }
    return convertValue
}

fun Int.dp(context: Context? = null): Int {
    return this.toFloat().dp(context)
}

fun Int.toDp(context: Context? = null): Float {
    return this.toFloat().toDp(context)
}

fun Int.sp(context: Context? = null): Int {
    return this.toFloat().sp(context)
}

fun Int.toSp(context: Context? = null): Float {
    return this.toFloat().toSp(context)
}

fun Int.mm(context: Context? = null): Int {
    return this.toFloat().mm(context)
}

fun Int.toMM(context: Context? = null): Float {
    return this.toFloat().toMM(context)
}

fun Context.dp(value: Float): Int {
    return value.dp(this)
}

fun Context.toDp(value: Float): Float {
    return value.toDp(this)
}

fun Context.sp(value: Float): Int {
    return value.sp(this)
}

fun Context.toSp(value: Float): Float {
    return value.toSp(this)
}

private val Fragment.compatContext: Context
    get() = context ?: AppInject.getApp()

fun Fragment.dp(value: Float): Int {
    return compatContext.dp(value)
}

fun Fragment.toDp(value: Float): Float {
    return compatContext.toDp(value)
}

fun Fragment.sp(value: Float): Int {
    return compatContext.sp(value)
}

fun Fragment.toSp(value: Float): Float {
    return compatContext.toSp(value)
}
// endregion

// region mm和px转换
fun Context.mm(value: Float): Int {
    return value.mm(this)
}

fun Context.toMm(value: Float): Float {
    return value.toMM(this)
}

fun Fragment.mm(value: Float): Int {
    return compatContext.mm(value)
}

fun Fragment.toMm(value: Float): Float {
    return compatContext.toMm(value)
}
// endregion