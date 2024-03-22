package com.github.xs93.framework.toast


/**
 * Toast 管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/11 15:37
 * @email 466911254@qq.com
 */
object ToastManager : IToast {

    private var mIToast: IToast? = null

    fun init(toast: IToast) {
        if (toast is ToastManager || toast is UiToastProxy) {
            return
        }
        mIToast = toast
    }

    override fun showToast(charSequence: CharSequence, duration: Int) {
        mIToast?.showToast(charSequence, duration)
    }

    override fun showToast(resId: Int, vararg formatArgs: Any?, duration: Int) {
        mIToast?.showToast(resId, formatArgs, duration)
    }
}