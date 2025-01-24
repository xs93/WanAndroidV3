package com.github.xs93.framework.base.ui.interfaces

/**
 * 软键盘监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/1/24 16:49
 * @email 466911254@qq.com
 */
interface ISoftKeyboardListener {
    /**
     * 软键盘状态改变,
     * @param show 软键盘是否显示
     * @param height 软键盘高度
     */
    fun onSoftKeyboardChanged(show: Boolean, height: Int)
}