package com.github.xs93.framework.core.ktx

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.Spanned
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * 单位尺寸扩展函数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/1 10:36
 * @email 466911254@qq.com
 */
fun Float.dp(context: Context? = null): Int {
    val resource = context?.resources ?: Resources.getSystem()
    val scale = resource.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Float.toDp(context: Context? = null): Float {
    val resource = context?.resources ?: Resources.getSystem()
    val scale = resource.displayMetrics.density
    return this / scale
}

fun Float.sp(context: Context? = null): Int {
    val resource = context?.resources ?: Resources.getSystem()
    val scale = resource.displayMetrics.scaledDensity
    return (this * scale + 0.5f).toInt()
}

fun Float.toSp(context: Context? = null): Float {
    val resource = context?.resources ?: Resources.getSystem()
    val scale = resource.displayMetrics.scaledDensity
    return this / scale
}


fun Int.dp(context: Context? = null): Int {
    val resource = context?.resources ?: Resources.getSystem()
    val scale = resource.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Int.toDp(context: Context? = null): Float {
    val resource = context?.resources ?: Resources.getSystem()
    val scale = resource.displayMetrics.density
    return this / scale
}

fun Int.sp(context: Context? = null): Int {
    val resource = context?.resources ?: Resources.getSystem()
    val scale = resource.displayMetrics.scaledDensity
    return (this * scale + 0.5f).toInt()
}


fun Int.toSp(context: Context? = null): Float {
    val resource = context?.resources ?: Resources.getSystem()
    val scale = resource.displayMetrics.scaledDensity
    return this / scale
}

@ColorInt
fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

fun Context.getHtml(@StringRes resId: Int): Spanned {
    return getString(resId).fromHtmlCompat()
}
