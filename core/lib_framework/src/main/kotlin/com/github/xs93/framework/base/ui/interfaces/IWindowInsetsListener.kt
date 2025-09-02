package com.github.xs93.framework.base.ui.interfaces

import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat


/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/5/29 16:38
 * @description WindowInsets 监听
 *
 */
interface IWindowInsetsListener {

    /**
     * 系统onApplyWindowInsets回调的调用,用于外部定制特殊处理
     */
    fun onWindowInsetsChanged(insets: WindowInsetsCompat) {}

    /**
     * 系统栏 insets 改变
     */
    fun onSystemBarInsetsChanged(insets: Insets) {}

    /**
     * 软键盘高度改变
     */
    fun onSoftKeyboardHeightChanged(imeVisible: Boolean, height: Int) {}
}