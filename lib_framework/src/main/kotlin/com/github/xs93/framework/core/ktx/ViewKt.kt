package com.github.xs93.framework.core.ktx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.appcompat.widget.Toolbar
import androidx.core.view.setMargins
import androidx.databinding.BindingAdapter

/**
 * View点击事件扩展事件
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/6/7 10:23
 * @email 466911254@qq.com
 */

@BindingAdapter("singleClickInterval", "onSingleClick", requireAll = false)
fun View.setSingleClickListener(
    singleClickInterval: Long? = 800,
    onSingleClick: View.OnClickListener? = null,
) {
    val interval = singleClickInterval ?: 800
    setOnClickListener(SingleClickListener(interval, onSingleClick))
}

@BindingAdapter("singleClickInterval", "onNavigationIconSingleClick", requireAll = false)
fun Toolbar.setNavigationIconSingleClickListener(
    singleClickInterval: Long? = 800,
    onSingleClick: View.OnClickListener? = null,
) {
    val interval = singleClickInterval ?: 800
    setNavigationOnClickListener(SingleClickListener(interval, onSingleClick))
}


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


@BindingAdapter("android:layout_width")
fun setLayoutWidth(view: View, width: Int) {
    view.layoutParams.apply {
        this.width = width
    }
    //调用此方法,防止有些手机界面初始化requestLayout()可能失效问题
    view.safeRequestLayout()
}

@BindingAdapter("android:layout_height")
fun setLayoutHeight(view: View, height: Int) {
    view.layoutParams.apply {
        this.height = height
    }
    //调用此方法,防止有些手机界面初始化requestLayout()可能失效问题
    view.safeRequestLayout()
}

@BindingAdapter("android:layout_marginStart")
fun View.setStartMargin(startMargin: Int) {
    val marginParams = layoutParams as ViewGroup.MarginLayoutParams
    marginParams.marginStart = startMargin
    safeRequestLayout()
}

@BindingAdapter("android:layout_marginTop")
fun View.setTopMargin(topMargin: Int) {
    val marginParams = layoutParams as ViewGroup.MarginLayoutParams
    marginParams.topMargin = topMargin
    safeRequestLayout()
}

@BindingAdapter("android:layout_marginEnd")
fun View.setEndMargin(endMargin: Int) {
    val marginParams = layoutParams as ViewGroup.MarginLayoutParams
    marginParams.marginEnd = endMargin
    safeRequestLayout()
}


@BindingAdapter("android:layout_marginBottom")
fun View.setBottomMargin(bottomMargin: Int) {
    val marginParams = layoutParams as ViewGroup.MarginLayoutParams
    marginParams.bottomMargin = bottomMargin
    safeRequestLayout()
}

@BindingAdapter("android:layout_margin")
fun View.setMargin(margin: Int) {
    val marginParams = layoutParams as ViewGroup.MarginLayoutParams
    marginParams.setMargins(margin)
    safeRequestLayout()
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