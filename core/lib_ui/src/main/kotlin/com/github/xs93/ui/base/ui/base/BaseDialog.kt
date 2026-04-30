package com.github.xs93.ui.base.ui.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.core.view.WindowCompat
import com.github.xs93.core.ktx.layoutInflater
import com.github.xs93.ui.R
import com.github.xs93.ui.base.ui.base.interfaces.IDialog
import com.github.xs93.ui.base.ui.base.interfaces.IWindowInsetsListener

/**
 * 基础Dialog基类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/22 17:26
 */
abstract class BaseDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0) :
    AppCompatDialog(context, themeResId), IDialog, IWindowInsetsListener {

    private val windowInsetsHelper = WindowInsetsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = onCreateView(context.layoutInflater, savedInstanceState)
        setContentView(onCreateView(layoutInflater, savedInstanceState))
        window?.let {
            initWindowStyle(it)
        }
        windowInsetsHelper.attach(view, this)
        initView(view, savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
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
    protected open fun onCreateView(inflater: LayoutInflater, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(contentLayoutId, null)
        return view
    }

    //region IBaseDialog
    override fun initWindowStyle(window: Window) {
        WindowCompat.enableEdgeToEdge(window)
    }

    override fun setWindowSize(width: Int, height: Int) {
        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = width
            layoutParams.height = height
            it.attributes = layoutParams
        }
    }

    override fun setFullScreenSize() {
        setWindowSize(-1, -1)
    }

    override fun setBottomDialog(isBottomDialog: Boolean) {
        window?.run {
            if (isBottomDialog) {
                setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
                setWindowAnimations(R.style.BaseBottomDialogWindowAnim)
            } else {
                setGravity(Gravity.CENTER)
            }
        }
    }
    //endregion
}