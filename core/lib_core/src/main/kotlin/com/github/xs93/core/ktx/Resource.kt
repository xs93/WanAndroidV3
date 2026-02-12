package com.github.xs93.core.ktx

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.TintTypedArray
import androidx.core.content.ContextCompat
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 单位尺寸扩展函数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/1 10:36
 * @email 466911254@qq.com
 */
val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)


@ColorInt
fun Context.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.colorStateList(@ColorRes id: Int): ColorStateList? {
    return ContextCompat.getColorStateList(this, id)
}

fun Context.drawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

fun Context.string(@StringRes id: Int, vararg formatArgs: Any?): String {
    return getString(id, *formatArgs)
}

fun Context.htmlString(@StringRes resId: Int): Spanned {
    return getString(resId).toHtml()
}

fun Context.quantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?): String {
    return resources.getQuantityString(id, quantity, *formatArgs)
}

@SuppressLint("RestrictedApi")
fun Context.obtainStyledAttributesCompat(
    set: AttributeSet? = null,
    @StyleableRes attrs: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
): TintTypedArray =
    TintTypedArray.obtainStyledAttributes(this, set, attrs, defStyleAttr, defStyleRes)

@OptIn(ExperimentalContracts::class)
@SuppressLint("RestrictedApi")
inline fun <R> TintTypedArray.use(block: (TintTypedArray) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return try {
        block(this)
    } finally {
        recycle()
    }
}

@SuppressLint("RestrictedApi")
fun Context.getBooleanByAttr(@AttrRes attr: Int): Boolean =
    obtainStyledAttributesCompat(attrs = intArrayOf(attr)).use { it.getBoolean(0, false) }

@ColorInt
fun Context.getColorByAttr(@AttrRes attr: Int): Int =
    getColorStateListByAttr(attr).defaultColor

@SuppressLint("RestrictedApi")
fun Context.getColorStateListByAttr(@AttrRes attr: Int): ColorStateList =
    obtainStyledAttributesCompat(attrs = intArrayOf(attr)).use { it.getColorStateList(0) }

@SuppressLint("RestrictedApi")
fun Context.getDimensionByAttr(@AttrRes attr: Int): Float =
    obtainStyledAttributesCompat(attrs = intArrayOf(attr)).use { it.getDimension(0, 0f) }

@SuppressLint("RestrictedApi")
fun Context.getDimensionPixelOffsetByAttr(@AttrRes attr: Int): Int =
    obtainStyledAttributesCompat(attrs = intArrayOf(attr)).use {
        it.getDimensionPixelOffset(0, 0)
    }

fun Context.withTheme(@StyleRes themeRes: Int): Context =
    if (themeRes != 0) ContextThemeWrapper(this, themeRes) else this