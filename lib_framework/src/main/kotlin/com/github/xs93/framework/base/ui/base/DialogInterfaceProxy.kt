package com.github.xs93.framework.base.ui.base

import android.content.DialogInterface
import java.lang.ref.WeakReference

/**
 * 代理弹出框监听器对象,防止内存泄漏
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/20 18:01
 */
class DialogInterfaceProxy {

    companion object {
        fun proxy(onCancelListener: DialogInterface.OnCancelListener?): DialogInterface.OnCancelListener {
            return ProxyOnCancelListener(onCancelListener)
        }

        fun proxy(onDismissListener: DialogInterface.OnDismissListener?): DialogInterface.OnDismissListener {
            return ProxyOnDismissListener(onDismissListener)
        }

        fun proxy(onShowListener: DialogInterface.OnShowListener?): DialogInterface.OnShowListener {
            return ProxyOnShowListener(onShowListener)
        }
    }
}

class ProxyOnCancelListener(listener: DialogInterface.OnCancelListener?) : DialogInterface.OnCancelListener {

    private var mProxyRef: WeakReference<DialogInterface.OnCancelListener>? = null

    init {
        listener?.let {
            mProxyRef = WeakReference(listener)
        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        val realListener = mProxyRef?.get()
        realListener?.onCancel(dialog)
    }
}

class ProxyOnDismissListener(listener: DialogInterface.OnDismissListener?) : DialogInterface.OnDismissListener {

    private var mProxyRef: WeakReference<DialogInterface.OnDismissListener>? = null

    init {
        listener?.let {
            mProxyRef = WeakReference(listener)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        val realListener = mProxyRef?.get()
        realListener?.onDismiss(dialog)
    }
}

class ProxyOnShowListener(listener: DialogInterface.OnShowListener?) : DialogInterface.OnShowListener {

    private var mProxyRef: WeakReference<DialogInterface.OnShowListener>? = null

    init {
        listener?.let {
            mProxyRef = WeakReference(listener)
        }
    }

    override fun onShow(dialog: DialogInterface?) {
        val realListener = mProxyRef?.get()
        realListener?.onShow(dialog)
    }
}