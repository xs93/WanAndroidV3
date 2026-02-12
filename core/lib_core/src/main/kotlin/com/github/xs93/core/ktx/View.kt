package com.github.xs93.core.ktx

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import com.github.xs93.core.utils.listener.SingleClickListener

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/1/19 9:34
 * @email 466911254@qq.com
 */


val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setSingleClickListener(
    singleClickInterval: Long = 800,
    bindOnSingleClick: View.OnClickListener? = null,
) {
    setOnClickListener(SingleClickListener(singleClickInterval, bindOnSingleClick))
}


/**判断当前View调用requestLayout是否有效*/
fun View.isSafeToRequestDirectly(): Boolean {
    return if (isInLayout) {
        isLayoutRequested.not()
    } else {
        var ancestorLayoutRequested = false
        var p: ViewParent? = parent
        while (p != null) {
            if (p.isLayoutRequested) {
                ancestorLayoutRequested = true
                break
            }
            p = p.parent
        }
        ancestorLayoutRequested.not()
    }
}


/** 安全调用requestLayout,保证界面肯定会被刷新 */
fun View.safeRequestLayout() {
    if (isSafeToRequestDirectly()) {
        requestLayout()
    } else {
        post { requestLayout() }
    }
}

/**
 * 显示输入法软键盘
 */
fun View.showSoftInput() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, 0)
}

/**
 * 隐藏输入法软键盘
 *
 * */
fun View.hideSoftInput() {
    if (windowToken != null) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

/**
 * 判断当前触摸事件是否在View的绘制区域
 * @receiver View
 * @param event MotionEvent
 * @return Boolean ，true 在这个区域,false 不再这个区域
 */
fun View.touchInRect(event: MotionEvent): Boolean {
    val local = IntArray(2)
    getLocationInWindow(local)
    val rect = Rect(local[0], local[1], local[0] + width, local[1] + height)
    return rect.contains(event.x.toInt(), event.y.toInt())
}

/**
 * View 显示在界面上的区域占自身区域的百分比
 */
val View.visiblePercentage: Float
    get() {
        if (width == 0 || height == 0) {
            return 0f
        }
        val rect = Rect()
        val visible = getLocalVisibleRect(rect)
        if (!visible) {
            return 0f
        }
        val visibleWidth = rect.width()
        val visibleHeight = rect.height()
        return (visibleWidth * visibleHeight) * 1.0f / (width * height)
    }