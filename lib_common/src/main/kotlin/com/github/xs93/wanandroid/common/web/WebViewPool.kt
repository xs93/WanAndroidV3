package com.github.xs93.wanandroid.common.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.MutableContextWrapper
import android.view.ViewGroup
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

    private val mAvailableWebViews: MutableList<WebView> = mutableListOf()
    private val mInUseWebViews: MutableList<WebView> = mutableListOf()

    fun init() {
        for (i in 0 until poolSize) {
            mAvailableWebViews.add(createNewWebView())
        }
    }

    @Synchronized
    fun acquireWebView(context: Context): WebView {
        val webView = if (mAvailableWebViews.size > 0) {
            val webView = mAvailableWebViews[0]
            mAvailableWebViews.remove(webView)
            mInUseWebViews.add(webView)
            webView
        } else {
            val webView = createNewWebView()
            mInUseWebViews.add(webView)
            webView
        }

        val webViewContext = webView.context
        if (webViewContext is MutableContextWrapper) {
            webViewContext.baseContext = context
        }
        return webView
    }

    @Synchronized
    fun releaseWebView(webView: WebView) {
        (webView.parent as ViewGroup?)?.removeView(webView)
        val webViewContext = webView.context
        if (webViewContext is MutableContextWrapper) {
            webViewContext.baseContext = AppInject.getApp()
        }
        webView.stopLoading()
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webView.clearHistory()
        webView.pauseTimers()
        webView.webChromeClient = null
        webView.webViewClient = WebViewClient()
        mInUseWebViews.remove(webView)
        if (mAvailableWebViews.size < poolSize) {
            mAvailableWebViews.add(webView)
        } else {
            webView.destroy()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createNewWebView(): WebView {
        val application = AppInject.getApp()
        val webView = WebView(MutableContextWrapper(application))
        webView.settings.javaScriptEnabled = true
        return webView
    }
}