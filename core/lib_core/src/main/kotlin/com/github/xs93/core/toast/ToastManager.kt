package com.github.xs93.core.toast

import android.widget.Toast
import androidx.annotation.StringRes
import com.github.xs93.core.AppInject
import com.github.xs93.core.toast.impl.SystemToast


/**
 * Toast 管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/11 15:37
 * @email 466911254@qq.com
 */
object ToastManager {

    private var mIToast: IToast? = null

    private val systemToast by lazy { SystemToast(AppInject.getApp()) }

    fun init(toast: IToast) {
        mIToast = toast
    }

    @JvmStatic
    fun showToast(charSequence: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        checkInit()
        mIToast?.showToast(charSequence, duration)
    }

    @JvmStatic
    fun showToast(
        @StringRes resId: Int,
        vararg formatArgs: Any?,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        checkInit()
        mIToast?.showToast(resId, formatArgs, duration)
    }


    private fun checkInit() {
        if (mIToast == null) {
            mIToast = systemToast
        }
    }
}