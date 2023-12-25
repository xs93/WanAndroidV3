package com.github.xs93.framework.toast

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

    override fun showToast(resId: Int, duration: Int) {
        ToastManager.showToast(resId, duration)
    }
}