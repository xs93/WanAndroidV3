package com.github.xs93.framework.base.ui.base

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatDialog
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy

/**
 * 代理监听器dialog,对应监听器使用弱引用,防止内存泄漏
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/20 18:11
 */
open class DialogInterfaceProxyDialog : AppCompatDialog, IToast {

    private val mIToast by lazy {
        UiToastProxy()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(
        context,
        cancelable,
        cancelListener
    )

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        super.setOnCancelListener(DialogInterfaceProxy.proxy(listener))
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(DialogInterfaceProxy.proxy(listener))
    }

    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        super.setOnShowListener(DialogInterfaceProxy.proxy(listener))
    }

    override fun showToast(charSequence: CharSequence, duration: Int) {
        mIToast.showToast(charSequence, duration)
    }

    override fun showToast(resId: Int, vararg formatArgs: Any?, duration: Int) {
        mIToast.showToast(resId, formatArgs, duration)
    }
}