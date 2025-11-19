package com.github.xs93.framework.base.ui.interfaces

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/11/19 10:53
 * @description
 *
 */
interface IDialogFragment {

    @get:LayoutRes
    val contentLayoutId: Int

    /** 是否沉浸式 */
    fun isImmersive(): Boolean

    /** 设置全屏 */
    fun setFullScreen()

    /** 设置底部弹窗 */
    fun setBottomDialog(isBottomDialog: Boolean)

    /** 是否是底部弹窗 */
    fun isBottomDialog(): Boolean

    /** 设置弹窗宽度 */
    fun setWindowWidth(width: Int)

    /** 弹窗高度 */
    fun setWindowHeight(height: Int)

    /** 初始化View */
    fun initView(view: View, savedInstanceState: Bundle?)

    /** 初始化订阅观察者 */
    fun initObserver(savedInstanceState: Bundle?) {}

    /** 初始化数据 */
    fun initData(savedInstanceState: Bundle?) {}
}