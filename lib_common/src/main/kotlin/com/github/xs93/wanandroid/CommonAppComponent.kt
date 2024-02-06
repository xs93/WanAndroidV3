package com.github.xs93.wanandroid

import android.app.Application
import android.content.Context
import com.github.xs93.framework.base.application.IAppComponent
import com.github.xs93.framework.base.ui.function.FunctionsManager
import com.github.xs93.framework.toast.ToastManager
import com.github.xs93.framework.toast.impl.SystemToast
import com.github.xs93.utils.ktx.appVersionCode
import com.github.xs93.utils.ktx.isDebug
import com.github.xs93.wanandroid.common.function.AutoSizeActivityFunction
import com.github.xs93.wanandroid.common.store.AppCommonStore
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSizeConfig

/**
 * CommonAppComponent 和 CommonApplication 相同实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/1/19 14:13
 * @email 466911254@qq.com
 */
class CommonAppComponent : IAppComponent {
    override fun onCreate(application: Application) {
        MMKV.initialize(application)
        initLogger(application)
        initAutoSize()
        initToast(application)
        checkInstallVersion(application)
        FunctionsManager.addCommonActivityFunctionClassPath(AutoSizeActivityFunction::class.java)
    }

    private fun initLogger(application: Application) {
        val prettyFormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(3)
            .methodOffset(0)
            .tag("xsLogger")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(prettyFormatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return application.isDebug
            }
        })
    }

    private fun initToast(context: Context) {
        val toast = SystemToast(context)
        ToastManager.init(toast)
    }

    private fun checkInstallVersion(application: Application) {
        if (AppCommonStore.appInstallVersionCode == -1L) {
            AppCommonStore.appInstallVersionCode = application.appVersionCode
        }
    }

    private fun initAutoSize() {
        AutoSizeConfig.getInstance().apply {
            isExcludeFontScale = true
        }
    }
}