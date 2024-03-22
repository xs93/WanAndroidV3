package com.github.xs93.framework.toast

import androidx.annotation.StringRes
import com.github.xs93.utils.AppInject

/**
 * Toast代理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/12 14:16
 * @email 466911254@qq.com
 */
class UiToastProxy : IToast {

    override fun showToast(charSequence: CharSequence, duration: Int) {
        ToastManager.showToast(charSequence, duration)
    }

    override fun showToast(@StringRes resId: Int, vararg formatArgs: Any?, duration: Int) {
        val toastContent = AppInject.getApp().getString(resId, formatArgs)
        ToastManager.showToast(toastContent, duration)
    }
}