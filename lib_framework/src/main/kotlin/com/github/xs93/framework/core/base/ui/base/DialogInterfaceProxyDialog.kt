package com.github.xs93.framework.core.base.ui.base

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatDialog
import com.github.xs93.framework.core.toast.IToast
import com.github.xs93.framework.core.toast.UiToastProxy

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

    override fun showToast(charSequence: CharSequence, duration: Int, vararg objects: Any) {
        mIToast.showToast(charSequence, duration, objects)
    }

    override fun showToast(resId: Int, duration: Int, vararg objects: Any) {
        mIToast.showToast(resId, duration, objects)
    }
}