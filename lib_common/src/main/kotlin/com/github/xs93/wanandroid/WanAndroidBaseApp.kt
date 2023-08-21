package com.github.xs93.wanandroid

import com.github.xs93.common.R
import com.github.xs93.framework.base.application.BaseApplication
import com.github.xs93.framework.toast.ToastManager
import com.github.xs93.framework.toast.impl.SystemToast
import com.github.xs93.network.EasyRetrofit
import com.github.xs93.network.exception.ExceptionHandler
import com.github.xs93.network.exception.ServiceApiException
import com.github.xs93.utils.ktx.isDebug
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * Application
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/19 17:06
 * @email 466911254@qq.com
 */
open class WanAndroidBaseApp : BaseApplication() {
    override fun addComponentApplication(classNames: MutableList<String>) {

    }

    override fun onCreate() {
        super.onCreate()

        val prettyFormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(3)
            .methodOffset(0)
            .tag("xsLogger")
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(prettyFormatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return isDebug
            }
        })

        val toast = SystemToast(this)
        ToastManager.init(toast)

        ExceptionHandler.safeRequestApiErrorHandler = {
            if (it is ServiceApiException) {
                ToastManager.showToast(it.errorMsg)
            } else {
                ToastManager.showToast(R.string.network_error)
            }
        }
        EasyRetrofit.init(this, "https://www.wanandroid.com/", null, isDebug)
    }
}