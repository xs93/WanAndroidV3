package com.github.xs93.framework.monitor

import android.app.Activity
import android.graphics.Rect
import android.view.View

/**
 * 监听软键盘收起与展开
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/2/15 20:40
 * @email 466911254@qq.com
 */
@Suppress("unused")
class SoftKeyBoardMonitor(activity: Activity) {


    private val rootView: View = activity.window.decorView

    private val outRect = Rect()

    private var rootViewVisibleHeight: Int = 0

    private var softKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null


    fun registerListener(listener: OnSoftKeyBoardChangeListener? = null) {
        softKeyBoardChangeListener = listener
    }

    init {
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            rootView.getWindowVisibleDisplayFrame(outRect)
            val visibleHeight = outRect.height()
            if (visibleHeight == 0) {
                return@addOnGlobalLayoutListener
            }
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            if (rootViewVisibleHeight == visibleHeight) {
                return@addOnGlobalLayoutListener
            }

            val delta = rootViewVisibleHeight - visibleHeight

            if (delta > 200) {
                softKeyBoardChangeListener?.keyBoardShow(delta)
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }

            if (delta < -200) {
                softKeyBoardChangeListener?.keyBoardHide(delta)
                rootViewVisibleHeight = visibleHeight
                return@addOnGlobalLayoutListener
            }
        }
    }


    interface OnSoftKeyBoardChangeListener {
        fun keyBoardShow(height: Int)
        fun keyBoardHide(height: Int)
    }
}