package com.github.xs93.framework.monitor.softkey

import androidx.lifecycle.Lifecycle

/**
 * 软键盘相关接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/4 13:14
 * @email 466911254@qq.com
 */
interface ISoftKeyBoardMonitor {

    fun registerListener(lifecycle: Lifecycle, listener: OnSoftKeyBoardChangeListener)

    fun registerListener(listener: OnSoftKeyBoardChangeListener)

    fun unregisterListener(listener: OnSoftKeyBoardChangeListener)
}