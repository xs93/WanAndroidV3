package com.github.xs93.framework.monitor

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.PopupWindow
import kotlin.math.abs

/**
 * 通过像Activity 添加一个宽度为0,高度填充满的PopupWindow，通过监听PopupWindow的高度变化来监听软键盘高度
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/4/19 13:42
 * @email 466911254@qq.com
 */
@Suppress("unused")
class SoftKeyBoardPopupWindowMonitor(private val activity: Activity) : PopupWindow(activity), OnGlobalLayoutListener {

    private var softKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null
    private val outRect = Rect()
    private var mRootViewMaxHeight: Int = 0

    private val mRootView: View = View(activity)

    init {
        contentView = mRootView
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        width = 0
        height = WindowManager.LayoutParams.MATCH_PARENT
        @Suppress("DEPRECATION")
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        inputMethodMode = INPUT_METHOD_NEEDED
    }


    fun start() {
        if (!isShowing && !activity.isDestroyed) {
            mRootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            mRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
            val decorView = activity.window.decorView
            decorView.post {
                if (!activity.isFinishing && !activity.isDestroyed && decorView.windowToken != null) {
                    showAtLocation(decorView, Gravity.NO_GRAVITY, 0, 0)
                }
            }
        }
    }

    fun registerListener(listener: OnSoftKeyBoardChangeListener? = null) {
        softKeyBoardChangeListener = listener
    }

    override fun onGlobalLayout() {
        mRootView.getWindowVisibleDisplayFrame(outRect)
        val visibleHeight = outRect.height()

        if (visibleHeight > mRootViewMaxHeight) {
            mRootViewMaxHeight = visibleHeight
        }

        val delta = mRootViewMaxHeight - visibleHeight
        softKeyBoardChangeListener?.keyBoardHeightChange(abs(delta))
    }

    interface OnSoftKeyBoardChangeListener {
        fun keyBoardHeightChange(height: Int)
    }
}