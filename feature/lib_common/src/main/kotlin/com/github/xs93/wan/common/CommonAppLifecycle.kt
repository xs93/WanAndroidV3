package com.github.xs93.wan.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Looper
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatDelegate
import com.github.xs93.coil.CoilManager
import com.github.xs93.core.base.app.IAppLifecycle
import com.github.xs93.core.crash.CrashHandler
import com.github.xs93.core.ktx.appVersionCode
import com.github.xs93.core.ktx.getColorByAttr
import com.github.xs93.core.ktx.isDebug
import com.github.xs93.core.toast.ToastManager
import com.github.xs93.core.toast.impl.SystemToast
import com.github.xs93.network.EasyRetrofit
import com.github.xs93.network.exception.LogErrorHandler
import com.github.xs93.network.okhttp.OkHttpClientManager
import com.github.xs93.ui.base.ui.function.FunctionsManager
import com.github.xs93.ui.loading.LoadingDialogHelper
import com.github.xs93.ui.loading.impl.DefaultCreateLoadingDialog
import com.github.xs93.wan.common.function.AutoSizeActivityFunction
import com.github.xs93.wan.common.function.EdgeToEdgeActivityFunction
import com.github.xs93.wan.common.network.WanErrorHandler
import com.github.xs93.wan.common.web.WebViewPool
import com.github.xs93.wan.data.DataConstant
import com.github.xs93.wan.data.store.AppCommonStore
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
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
class CommonAppLifecycle : IAppLifecycle {

    private val crashHandler by lazy { CrashHandler() }

    override fun onCreate(application: Application) {
        LoadingDialogHelper.initLoadingDialog(DefaultCreateLoadingDialog())
        crashHandler.init(application)
        initLogger(application)
        MMKV.initialize(application)
        initAutoSize()
        initToast(application)
        checkInstallVersion(application)
        FunctionsManager.addCommonActivityFunctionClassPath(AutoSizeActivityFunction::class.java)
        FunctionsManager.addCommonActivityFunctionClassPath(EdgeToEdgeActivityFunction::class.java)
        CoilManager.init(application, OkHttpClientManager.getBaseClient())
        initHttp(application)
        initSmartRefreshLayout()
        initThemeMode()
        initWebViewPool()
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
            setExcludeFontScale(true)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initHttp(application: Application) {
        EasyRetrofit.init(application)
        EasyRetrofit.addErrorHandler(LogErrorHandler())
        EasyRetrofit.addErrorHandler(WanErrorHandler())
        EasyRetrofit.createRetrofit(DataConstant.BASE_URL)
    }

    private fun initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setPrimaryColors(
                context.getColorByAttr(R.attr.colorPrimary),
                context.getColorByAttr(R.attr.colorPrimary)
            )
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context).apply {
                setColorSchemeColors(context.getColorByAttr(R.attr.colorPrimary))
            }
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            BallPulseFooter(context)
        }
    }

    private fun initThemeMode() {
        if (AppCommonStore.userCustomNightMode) {
            if (AppCommonStore.isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun initWebViewPool() {
        Looper.getMainLooper().queue.addIdleHandler {
            WebViewPool.init()
            return@addIdleHandler false
        }
    }
}