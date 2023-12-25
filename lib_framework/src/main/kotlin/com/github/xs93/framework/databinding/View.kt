package com.github.xs93.framework.databinding

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.setMargins
import androidx.databinding.BindingAdapter
import com.github.xs93.framework.ktx.SingleClickListener
import com.github.xs93.framework.ktx.safeRequestLayout

/**
 * View的通用dataBinding BindAdapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/12/18 11:14
 * @email 466911254@qq.com
 */

@BindingAdapter("bindSingleClickInterval", "bindOnSingleClick", requireAll = false)
fun bindSingleClickListener(
    view: View,
    singleClickInterval: Long? = 1000,
    onSingleClick: View.OnClickListener? = null,
) {
    val interval = singleClickInterval ?: 1000
    if (view is Toolbar) {
        view.setNavigationOnClickListener(SingleClickListener(interval, onSingleClick))
    } else {
        view.setOnClickListener(SingleClickListener(interval, onSingleClick))
    }
}

@BindingAdapter("android:layout_marginStart")
fun bindStartMargin(view: View, startMargin: Int) {
    val marginParams = view.layoutParams as ViewGroup.MarginLayoutParams
    marginParams.marginStart = startMargin
    view.safeRequestLayout()
}

@BindingAdapter("android:layout_marginTop")
fun bindTopMargin(view: View, topMargin: Int) {
    val marginParams = view.layoutParams as ViewGroup.MarginLayoutParams
    marginParams.topMargin = topMargin
    view.safeRequestLayout()
}

@BindingAdapter("android:layout_marginEnd")
fun bindEndMargin(view: View, endMargin: Int) {
    val marginParams = view.layoutParams as ViewGroup.MarginLayoutParams
    marginParams.marginEnd = endMargin
    view.safeRequestLayout()
}


@BindingAdapter("android:layout_marginBottom")
fun bindBottomMargin(view: View, bottomMargin: Int) {
    val marginParams = view.layoutParams as ViewGroup.MarginLayoutParams
    marginParams.bottomMargin = bottomMargin
    view.safeRequestLayout()
}

@BindingAdapter("android:layout_margin")
fun bindMargin(view: View, margin: Int) {
    val marginParams = view.layoutParams as ViewGroup.MarginLayoutParams
    marginParams.setMargins(margin)
    view.safeRequestLayout()
}