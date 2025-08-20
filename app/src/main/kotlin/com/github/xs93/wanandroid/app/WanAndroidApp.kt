package com.github.xs93.wanandroid.app

import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.github.xs93.coil.CoilManager
import com.github.xs93.framework.crash.CrashHandler
import com.github.xs93.network.EasyRetrofit
import com.github.xs93.network.exception.LogErrorHandler
import com.github.xs93.network.okhttp.OkHttpClientManager
import com.github.xs93.utils.ktx.getColorByAttr
import com.github.xs93.utils.net.NetworkMonitor
import com.github.xs93.wanandroid.AppConstant
import com.github.xs93.wanandroid.CommonApplication
import com.github.xs93.wanandroid.common.network.WanErrorHandler
import com.github.xs93.wanandroid.common.store.AppCommonStore
import com.github.xs93.wanandroid.common.web.WebViewPool
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application 实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 10:11
 * @email 466911254@qq.com
 */
@HiltAndroidApp
class WanAndroidApp : CommonApplication() {

    @Inject
    lateinit var webViewPool: WebViewPool

    private val crashHandler by lazy { CrashHandler() }

    override fun onCreate() {
        super.onCreate()
        crashHandler.init(this)
        CoilManager.init(this, OkHttpClientManager.getBaseClient())
        initHttp()
        initSmartRefreshLayout()
        initThemeMode()
        initWebViewPool()
    }

    private fun initHttp() {
        NetworkMonitor.init(this)
        EasyRetrofit.init(this)
        EasyRetrofit.addErrorHandler(LogErrorHandler())
        EasyRetrofit.addErrorHandler(WanErrorHandler())
        EasyRetrofit.createRetrofit(AppConstant.BaseUrl)
    }

    private fun initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setPrimaryColors(
                context.getColorByAttr(androidx.appcompat.R.attr.colorPrimary),
                context.getColorByAttr(androidx.appcompat.R.attr.colorPrimary)
            )
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context).apply {
                setColorSchemeColors(context.getColorByAttr(androidx.appcompat.R.attr.colorPrimary))
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
            webViewPool.init()
            return@addIdleHandler false
        }
    }
}