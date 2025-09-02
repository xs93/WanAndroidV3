package com.github.xs93.network.cookie

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 *
 * Cookie保存接口
 *
 * @author xushuai
 * @date   2022/9/2-17:10
 * @email  466911254@qq.com
 */
interface CookieStore {

    fun saveCookie(url: HttpUrl, cookies: List<Cookie>)

    fun saveCookie(url: HttpUrl, cookie: Cookie)

    fun loadCookie(url: HttpUrl): List<Cookie>

    fun getAllCookie(): List<Cookie>

    fun getCookie(url: HttpUrl): List<Cookie>

    fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean

    fun removeCookie(url: HttpUrl): Boolean

    fun removeAllCookie(): Boolean
}