package com.github.xs93.wanandroid.web.webclient

import android.annotation.SuppressLint
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.just.agentweb.WebViewClient


/**
 * WebViewClient默认实现,拦截了一些必要的广告网站
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/3/15
 */
open class BaseWebClient : WebViewClient() {

    // 拦截的网址
    private val blackHostList = arrayListOf(
        "www.taobao.com",
        "www.jd.com",
        "yun.tuisnake.com",
        "yun.lvehaisen.com",
        "yun.tuitiger.com",
        "bilibili://video/",
        //广告
        "https://api.interactive.xianyujoy.cn",
    )

    private val blackUrlString = arrayListOf(
        "bilibili://video/"
    )

    private fun isBlackHost(host: String): Boolean {
        for (blackHost in blackHostList) {
            if (blackHost == host) {
                return true
            }
        }
        return false
    }

    private fun isBlackUrl(uri: Uri): Boolean {
        val uriString = uri.toString()
        for (blackUri in blackUrlString) {
            if (uriString.startsWith(blackUri)) {
                return true
            }
        }
        return false
    }

    private fun shouldInterceptRequest(uri: Uri?): Boolean {
        if (uri != null) {
            val host = uri.host ?: ""
            return isBlackHost(host) || isBlackUrl(uri)
        }
        return false
    }

    private fun shouldOverrideUrlLoading(uri: Uri?): Boolean {
        if (uri != null) {
            val host = uri.host ?: ""
            return isBlackHost(host) || isBlackUrl(uri)
        }
        return false
    }


    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        if (shouldInterceptRequest(request?.url)) {
            return WebResourceResponse(null, null, null)
        }
        return super.shouldInterceptRequest(view, request)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        if (shouldInterceptRequest(Uri.parse(url))) {
            return WebResourceResponse(null, null, null)
        }
        return super.shouldInterceptRequest(view, url)
    }

    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Log.d("WebClient", "$url")
        return shouldOverrideUrlLoading(Uri.parse(url))
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val uri = request?.url
        Log.d("WebClient", uri.toString())
        return shouldOverrideUrlLoading(uri)
    }

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//        super.onReceivedSslError(view, handler, error)
        handler?.proceed()
    }
}