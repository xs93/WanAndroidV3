package com.github.xs93.framework.core.toast

import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 对外Toast接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/12 14:17
 * @email 466911254@qq.com
 */
object ToastHelper {

    private var mIToast: IToast? = null

    fun initToast(toast: IToast) {
        mIToast = toast
    }

    @JvmStatic
    fun showToast(charSequence: CharSequence, duration: Int = Toast.LENGTH_SHORT, vararg objects: Any) {
        mIToast?.showToast(charSequence, duration, objects)
    }

    @JvmStatic
    fun showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT, vararg objects: Any) {
        mIToast?.showToast(resId, duration, objects)
    }
}