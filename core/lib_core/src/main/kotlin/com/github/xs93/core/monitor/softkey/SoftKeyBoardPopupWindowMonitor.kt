package com.github.xs93.core.monitor.softkey

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.abs

/**
 * 通过给activity添加一个宽度为0,,高度占满的popupWindow,通过监听popupWindow的高度变化来监听软键盘的高度变化
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/31 14:32
 * @email 466911254@qq.com
 */
class SoftKeyBoardPopupWindowMonitor(val activity: Activity) : PopupWindow(activity),
    ViewTreeObserver.OnGlobalLayoutListener, ISoftKeyBoardMonitor {

    private val mListeners: MutableList<OnSoftKeyBoardChangeListener> = mutableListOf()

    private var mRootViewMaxHeight: Int = 0
    private val mOutRect = Rect()
    private val mRootView: View = View(activity)

    init {
        contentView = mRootView
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        width = 0
        height = WindowManager.LayoutParams.MATCH_PARENT
        @Suppress("DEPRECATION")
        softInputMode =
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
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


    override fun onGlobalLayout() {
        mRootView.getWindowVisibleDisplayFrame(mOutRect)
        val visibleHeight = mOutRect.height()

        if (visibleHeight > mRootViewMaxHeight) {
            mRootViewMaxHeight = visibleHeight
        }

        val delta = mRootViewMaxHeight - visibleHeight
        synchronized(mListeners) {
            mListeners.forEach {
                it.keyBoardHeightChange(abs(delta))
            }
        }
    }

    override fun registerListener(listener: OnSoftKeyBoardChangeListener) {
        listener.let {
            synchronized(mListeners) {
                if (!mListeners.contains(it)) {
                    mListeners.add(it)
                }
            }
        }
    }

    override fun unregisterListener(listener: OnSoftKeyBoardChangeListener) {
        listener.let {
            synchronized(mListeners) {
                mListeners.remove(it)
            }
        }
    }

    override fun registerListener(lifecycle: Lifecycle, listener: OnSoftKeyBoardChangeListener) {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        registerListener(listener)
                    }

                    Lifecycle.Event.ON_STOP -> {
                        unregisterListener(listener)
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        lifecycle.removeObserver(this)
                        dismiss()
                    }

                    else -> {}
                }
            }
        })
    }
}