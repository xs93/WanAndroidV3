package com.github.xs93.framework.base.ui.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import androidx.core.view.WindowCompat
import com.github.xs93.framework.R
import com.github.xs93.framework.base.ui.interfaces.IDialog
import com.github.xs93.framework.base.ui.interfaces.IWindowInsetsListener
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy

/**
 * 基础Dialog基类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/22 17:26
 */
abstract class BaseDialog @JvmOverloads constructor(
    context: Context,
    themeResId: Int = R.style.BaseDialogTheme
) : AppCompatDialog(context, themeResId), IDialog, IToast, IWindowInsetsListener {

    private val iToast by lazy { UiToastProxy() }

    private val windowInsetsHelper = WindowInsetsHelper()

    private var isBottomDialog: Boolean = false
    private var windowWidth: Int = -2
    private var windowHeight: Int = -2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutInflater = LayoutInflater.from(context)
        val view = onCreateView(layoutInflater, savedInstanceState)
        setContentView(onCreateView(layoutInflater, savedInstanceState))
        window?.let {
            WindowCompat.enableEdgeToEdge(it)
        }
        windowInsetsHelper.attach(view, this)
        initView(view, savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        window?.let {
            setBottomDialog(isBottomDialog)
            setWindowWidth(windowWidth)
            setWindowHeight(windowHeight)
        }
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener(DialogInterfaceProxy.proxy(listener))
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(DialogInterfaceProxy.proxy(listener))
    }

    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        super.setOnShowListener(DialogInterfaceProxy.proxy(listener))
    }

    /**
     * 创建View
     */
    protected open fun onCreateView(
        layoutInflater: LayoutInflater,
        savedInstanceState: Bundle?
    ): View {
        val view = layoutInflater.inflate(contentLayoutId, null)
        return view
    }

    //region IBaseDialog
    override fun setFullScreen() {
        windowWidth = WindowManager.LayoutParams.MATCH_PARENT
        windowHeight = WindowManager.LayoutParams.MATCH_PARENT
        val window = window ?: return
        val layoutParams = window.attributes
        layoutParams.width = windowWidth
        layoutParams.height = windowHeight
        window.attributes = layoutParams
    }

    override fun setBottomDialog(isBottomDialog: Boolean) {
        this.isBottomDialog = isBottomDialog
        val window = window ?: return
        if (isBottomDialog) {
            window.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            window.setWindowAnimations(R.style.BaseBottomDialogWindowAnim)
        } else {
            window.setGravity(Gravity.CENTER)
        }
    }

    override fun isBottomDialog(): Boolean {
        return isBottomDialog
    }

    override fun setWindowWidth(width: Int) {
        windowWidth = width
        val window = window ?: return
        val layoutParams = window.attributes
        layoutParams.width = windowWidth
        window.attributes = layoutParams
    }

    override fun setWindowHeight(height: Int) {
        windowHeight = height
        val window = window ?: return
        val layoutParams = window.attributes
        layoutParams.height = windowHeight
        window.attributes = layoutParams
    }
    //endregion

    //region IToast
    override fun showToast(charSequence: CharSequence, duration: Int) {
        iToast.showToast(charSequence, duration)
    }

    override fun showToast(resId: Int, vararg formatArgs: Any?, duration: Int) {
        iToast.showToast(resId, formatArgs, duration)
    }
    //endregion
}