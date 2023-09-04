package com.github.xs93.wanandroid.web.webclient

import com.just.agentweb.WebViewClient


/**
 *  WebViewClient工厂类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/3/15
 */
object WebClientFactory {
    const val JIAN_SHU = "https://www.jianshu.com"

    @JvmStatic
    fun create(url: String?): WebViewClient {
        return when {
            url?.startsWith(JIAN_SHU) == true -> JianShuWebClient()
            else -> BaseWebClient()
        }
    }
}