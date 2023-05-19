package com.github.xs93.framework.network.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 *
 * 实现CookieJar接口，管理接口请求的cookie值
 *
 * @author xushuai
 * @date   2022/9/2-17:07
 * @email  466911254@qq.com
 */
class CookieJarManager(private val cookieStore: CookieStore) : CookieJar {

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore.loadCookie(url)
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.saveCookie(url, cookies)
    }
}