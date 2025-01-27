package com.github.xs93.framework.base.ui.interfaces

import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 软键盘 window insets callback,使用window insets动画回调，监听软键盘弹出和收起动画
 *
 * 注意:windowSoftInputMode 设置为adjustResize,可以有效覆大部分低版本兼容问题
 * 在Dialog和DialogFragment中,使用windowSoftInputMode 设置为adjustResize,可以有效覆大部分低版本兼容问题
 * 如果需要动画,则dialog 或者 dialogFragment 显示的时候,最好使用全屏高度，可以避免动画卡顿,使动画更流畅
 *
 *
 * 在 API 级别为 30+ 的设备上运行时，此功能可在 IME 进入/退出屏幕时完美跟踪 IME。
 * 在 API 级别为 21-29 的设备上运行时，WindowInsetsAnimationCompat 将运行一个动画，该动画会尝试模拟系统 IME 动画。这永远无法完美地跟踪 IME，但应该为用户提供愉快的体验。
 * 当运行具有 API < 21 的设备时，动画根本不会运行，并且会恢复为即时“对齐”。
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/1/24 16:55
 * @email 466911254@qq.com
 */
class SoftKeyboardInsetsCallback(
    dispatchMode: Int = DISPATCH_MODE_CONTINUE_ON_SUBTREE,
    val listener: ISoftKeyboardListener? = null,
) : WindowInsetsAnimationCompat.Callback(dispatchMode),
    OnApplyWindowInsetsListener,
    ISoftKeyboardListener {

    private var view: View? = null
    private var lastWindowInsets: WindowInsetsCompat? = null
    private var deferredInsets: Boolean = false

    private var lastImeVisible: Boolean = false
    private var lastImeHeight: Int = 0

    fun attachToView(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view, this)
        ViewCompat.setWindowInsetsAnimationCallback(view, this)
    }

    /**
     * 会在onPrepare 之后调用,onStart 之前调用，可以获取最终软键盘的高度和状态
     */
    override fun onApplyWindowInsets(
        v: View,
        insets: WindowInsetsCompat,
    ): WindowInsetsCompat {
        // 保存view和window insets,当软键盘弹出动画结束以后,可以校准软键盘高度和显示状态,
        view = v
        lastWindowInsets = insets
        return WindowInsetsCompat.CONSUMED
    }

    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
        if ((animation.typeMask and WindowInsetsCompat.Type.ime()) != 0) {
            deferredInsets = true
        }
    }

    override fun onStart(
        animation: WindowInsetsAnimationCompat,
        bounds: WindowInsetsAnimationCompat.BoundsCompat,
    ): WindowInsetsAnimationCompat.BoundsCompat {
        return super.onStart(animation, bounds)
    }

    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnimations: List<WindowInsetsAnimationCompat?>,
    ): WindowInsetsCompat {
        if (deferredInsets) {
            computeImHeight(insets)
        }
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        super.onEnd(animation)
        if (deferredInsets && (animation.typeMask and WindowInsetsCompat.Type.ime() != 0)) {
            deferredInsets = false
            lastWindowInsets?.let {
                computeImHeight(it)
            }
        }
    }

    override fun onSoftKeyboardChanged(show: Boolean, height: Int) {
        listener?.onSoftKeyboardChanged(show, height)
    }

    private fun computeImHeight(insets: WindowInsetsCompat) {
        val imeInset = insets.getInsets(WindowInsetsCompat.Type.ime())
        val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
        val navigationBarVisible = insets.isVisible(WindowInsetsCompat.Type.navigationBars())
        var imeHeight = imeInset.bottom
        var resultImeHeight = imeHeight
        // 注意导航栏显示时高度计算和内容高度计算
        if (navigationBarVisible) {
            val navigationBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val navigationBarHeight = navigationBarInset.bottom
            resultImeHeight -= navigationBarHeight
        }
        resultImeHeight = resultImeHeight.coerceAtLeast(0)
        // 对比一下，不要重复调用
        if (imeVisible != lastImeVisible || resultImeHeight != lastImeHeight) {
            lastImeVisible = imeVisible
            lastImeHeight = resultImeHeight
            onSoftKeyboardChanged(imeVisible, resultImeHeight)
        }
    }
}