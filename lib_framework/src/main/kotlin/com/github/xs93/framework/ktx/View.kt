package com.github.xs93.framework.ktx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.appcompat.widget.Toolbar

/**
 * View点击事件扩展事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/6/7 10:23
 * @email 466911254@qq.com
 */

class SingleClickListener(private val interval: Long = 1000, private val singleClick: View.OnClickListener? = null) :
    View.OnClickListener {

    private var mLastClickTime = 0L
    override fun onClick(v: View) {
        if (System.currentTimeMillis() - mLastClickTime > interval) {
            singleClick?.onClick(v)
        }
        mLastClickTime = System.currentTimeMillis()
    }
}

fun View.setSingleClickListener(
    singleClickInterval: Long? = 800,
    onSingleClick: View.OnClickListener? = null,
) {
    val interval = singleClickInterval ?: 800
    setOnClickListener(SingleClickListener(interval, onSingleClick))
}

fun Toolbar.setNavigationIconSingleClickListener(
    singleClickInterval: Long? = 800,
    onSingleClick: View.OnClickListener? = null,
) {
    val interval = singleClickInterval ?: 800
    setNavigationOnClickListener(SingleClickListener(interval, onSingleClick))
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

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ViewGroup.inflate(layoutResId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutResId, this, attachToRoot)
}