package com.github.xs93.framework.core.toast

/**
 * Toast代理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/12 14:16
 * @email 466911254@qq.com
 */
class UiToastProxy : IToast {

    override fun showToast(charSequence: CharSequence, duration: Int, vararg objects: Any) {
        ToastHelper.showToast(charSequence, duration, objects)
    }

    override fun showToast(resId: Int, duration: Int, vararg objects: Any) {
        ToastHelper.showToast(resId, duration, objects)
    }
}