package com.github.xs93.wanandroid.common.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.MutableContextWrapper
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.xs93.utils.AppInject
import javax.inject.Inject

/**
 * WebView缓存池
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/15 14:46
 * @email 466911254@qq.com
 */
class WebViewPool @Inject constructor(private val poolSize: Int) {

    private val mAvailableWebViews = arrayOfNulls<WebView>(poolSize)
    private val mInUseWebViews: MutableList<WebView> = mutableListOf()

    @Volatile
    private var fullPooled: Boolean = false

    fun init() {
        fullPools()
    }

    @Synchronized
    fun acquireWebView(context: Context): WebView {
        if (!fullPooled) {
            fullPools()
        }
        var targetWebView: WebView? = null
        for (i in 0 until poolSize) {
            val webView = mAvailableWebViews[i]
            if (webView != null) {
                targetWebView = webView
                mAvailableWebViews[i] = null
                break
            }
        }

        if (targetWebView == null) {
            targetWebView = createNewWebView()
        }

        val webViewContext = targetWebView.context
        if (webViewContext is MutableContextWrapper) {
            webViewContext.baseContext = context
        }
        mInUseWebViews.add(targetWebView)
        return targetWebView
    }

    @Synchronized
    fun releaseWebView(webView: WebView) {
        webView.stopLoading()
        webView.pauseTimers()
        (webView.parent as ViewGroup?)?.removeView(webView)
        mInUseWebViews.remove(webView)
        if (getAvailableWebViewSize() < poolSize) {
            val webViewContext = webView.context
            if (webViewContext is MutableContextWrapper) {
                webViewContext.baseContext = AppInject.getApp()
            }
            defaultSettings(webView)
            // 这样才能完全清除旧的历史记录
            webView.webViewClient = object : WebViewClient() {
                override fun doUpdateVisitedHistory(
                    view: WebView?,
                    url: String?,
                    isReload: Boolean
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                    webView.clearHistory()
                    // 重设一个WebViewClient,防止用户不设置导致历史记录被清除
                    webView.webViewClient = WebViewClient()
                }
            }
            webView.webChromeClient = null
            webView.loadUrl(WrapWebView.DEFAULT_URL)

            for (i in 0 until poolSize) {
                if (mAvailableWebViews[i] == null) {
                    mAvailableWebViews[i] = webView
                    break
                }
            }
            fullPools()
        } else {
            webView.destroy()
        }
    }


    @Synchronized
    private fun fullPools() {
        for (i in 0 until poolSize) {
            val webView = mAvailableWebViews[i]
            if (webView == null) {
                mAvailableWebViews[i] = createNewWebView()
            }
        }
        fullPooled = true
    }

    private fun createNewWebView(): WebView {
        val application = AppInject.getApp()
        val webView = WrapWebView(MutableContextWrapper(application))
        defaultSettings(webView)
        return webView
    }


    private fun getAvailableWebViewSize(): Int {
        var size = 0
        mAvailableWebViews.forEach {
            if (it != null) size++
        }
        return size
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun defaultSettings(webView: WebView) {
        webView.setBackgroundColor(Color.WHITE)
        webView.overScrollMode = View.OVER_SCROLL_NEVER
        webView.isNestedScrollingEnabled = false

        with(webView.settings) {
            // 设置自适应屏幕,两者结合使用
            useWideViewPort = true
            loadWithOverviewMode = true
            // 是否支持缩放，默认为true
            setSupportZoom(true)
            // 是否使用内置的缩放控件
            builtInZoomControls = false
            // 是否显示原生的缩放控件
            displayZoomControls = false
            // 设置文本缩放 默认 100
            textZoom = 100
            // 是否可以访问文件
            allowFileAccess = true
            // 是否启用 database storage API
            @Suppress("DEPRECATION")
            databaseEnabled = true
            // 这次JavaScript
            javaScriptEnabled = true
            // 是否支持通过js打开新窗口
            javaScriptCanOpenWindowsAutomatically = true
            // 是否启用 DOM Storage API
            domStorageEnabled = true
            // 是否支持自动加载图片
            loadsImagesAutomatically = true
            blockNetworkImage = false
            // 设置编码格式
            defaultTextEncodingName = "utf-8"
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            // 配置当安全源试图从不安全加载资源时WebView的行为，5.1以上默认禁止了https和http混用，以下方式是开启
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            // 设置缓存模式
            cacheMode = WebSettings.LOAD_DEFAULT
        }
    }
}