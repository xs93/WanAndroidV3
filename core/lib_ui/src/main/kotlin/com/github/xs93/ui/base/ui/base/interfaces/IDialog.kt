package com.github.xs93.ui.base.ui.base.interfaces

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.LayoutRes

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/11/14 13:26
 * @description 弹窗接口
 *
 */
interface IDialog {

    @get:LayoutRes
    val contentLayoutId: Int

    /** 设置弹窗大小 */
    fun setWindowSize(width: Int, height: Int)

    /** 设置全屏 */
    fun setFullScreenSize()

    /** 设置底部弹窗 */
    fun setBottomDialog(isBottomDialog: Boolean)

    /** 初始化弹窗 */
    fun initWindowStyle(window: Window)

    /** 初始化View */
    fun initView(view: View, savedInstanceState: Bundle?)

    /** 初始化订阅观察者 */
    fun initObserver(savedInstanceState: Bundle?) {}

    /** 初始化数据 */
    fun initData(savedInstanceState: Bundle?) {}
}