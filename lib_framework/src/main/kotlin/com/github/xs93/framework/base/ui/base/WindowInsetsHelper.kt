package com.github.xs93.framework.base.ui.base

import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import com.github.xs93.framework.base.ui.interfaces.IWindowInsetsListener

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/5/29 16:37
 * @description Window Insets 监听帮助类
 *
 */
class WindowInsetsHelper(dispatchMode: Int = DISPATCH_MODE_CONTINUE_ON_SUBTREE) :
    WindowInsetsAnimationCompat.Callback(dispatchMode), OnApplyWindowInsetsListener {

    companion object {
        private const val TAG = "WindowInsetsHelper"
    }

    private var listener: IWindowInsetsListener? = null

    private var applyWindowView: View? = null
    private var lastWindowInsets: WindowInsetsCompat? = null
    private var deferredInsets: Boolean = false

    private var lastImeVisible: Boolean = false
    private var lastImeHeight: Int = 0


    fun attach(view: View, listener: IWindowInsetsListener) {
        this.listener = listener
        ViewCompat.setOnApplyWindowInsetsListener(view, this)
        ViewCompat.setWindowInsetsAnimationCallback(view, this)
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        // 保存view和window insets,当软键盘弹出动画结束以后,可以校准软键盘高度和显示状态,
        applyWindowView = v
        lastWindowInsets = WindowInsetsCompat(insets)

        val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        listener?.onSystemBarInsetsChanged(systemBarsInsets)

        /*当前回调之前没有执行软键盘动画并且软键盘显示,则计算软键盘高度和状态,因为软键盘功能可能导致软键盘高度发生变化
        当在dialog中使用时,activity中注册的OnApplyWindowInsetsListener也会回调，故 lastImeVisible可以过滤activity的错误调用
        */
        if (!deferredInsets && lastImeVisible) {
            computeImeHeight(insets)
        }
        return insets
    }

    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
        if ((animation.typeMask and WindowInsetsCompat.Type.ime()) != 0) {
            deferredInsets = true
        }
    }

    override fun onProgress(
        insets: WindowInsetsCompat,
        runningAnimations: List<WindowInsetsAnimationCompat?>
    ): WindowInsetsCompat {
        if (deferredInsets) {
            computeImeHeight(insets)
        }
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimationCompat) {
        super.onEnd(animation)
        if (deferredInsets && (animation.typeMask and WindowInsetsCompat.Type.ime() != 0)) {
            deferredInsets = false
            lastWindowInsets?.let {
                computeImeHeight(it)
            }
        }
    }

    /**
     * 计算软键盘高度
     */
    private fun computeImeHeight(insets: WindowInsetsCompat) {
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
            listener?.onSoftKeyboardHeightChanged(imeVisible, resultImeHeight)
        }
    }
}