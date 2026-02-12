package com.github.xs93.ui.base.ui.interfaces

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/11/19 16:29
 * @description BottomSheetDialogFragment 接口
 *
 */
interface IBottomSheetDialogFragment {

    @get:LayoutRes
    val contentLayoutId: Int

    /** 初始化View */
    fun initView(view: View, savedInstanceState: Bundle?)

    /** 初始化订阅观察者 */
    fun initObserver(savedInstanceState: Bundle?) {}

    /** 初始化数据 */
    fun initData(savedInstanceState: Bundle?) {}
}