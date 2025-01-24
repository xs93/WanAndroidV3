package com.github.xs93.framework.base.ui.interfaces

import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 软键盘 window insets callback
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