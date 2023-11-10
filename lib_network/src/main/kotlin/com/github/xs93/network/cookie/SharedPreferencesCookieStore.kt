package com.github.xs93.network.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * 使用SharedPreferences保存的Cookie实现
 *
 * @author xushuai
 * @date   2022/9/2-23:05
 * @email  466911254@qq.com
 */
class SharedPreferencesCookieStore(context: Context) : CookieStore {

    companion object {

        private const val SP_NAME = "sp_save_cookie"
        private const val COOKIE_NAME_PREFIX = "cookie_"
    }

    private val cookieSP: SharedPreferences =
        context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    private val cookies: HashMap<String, ConcurrentHashMap<String, Cookie>> = HashMap()


    @Synchronized
    override fun saveCookie(url: HttpUrl, cookies: List<Cookie>) {
        for (cookie in cookies) {
            saveCookie(url, cookie)
        }
    }

    @Synchronized
    override fun saveCookie(url: HttpUrl, cookie: Cookie) {
        if (!cookies.containsKey(url.host) || cookies[url.host] == null) {
            cookies[url.host] = ConcurrentHashMap<String, Cookie>()
        }
        if (cookie.isExpired()) {
            removeCookie(url, cookie)
        } else {
            saveCookie(url, cookie, cookie.token)
        }
    }

    @Synchronized
    override fun loadCookie(url: HttpUrl): List<Cookie> {
        if (!cookies.containsKey(url.host)) return emptyList()
        val result = arrayListOf<Cookie>()
        val urlCookies = cookies[url.host]!!.values
        for (cookie in urlCookies) {
            if (cookie.isExpired()) {
                removeCookie(url, cookie)
            } else {
                result.add(cookie)
            }
        }
        return result
    }

    @Synchronized
    override fun getAllCookie(): List<Cookie> {
        val result = arrayListOf<Cookie>()
        for (name in cookies.keys) {
            cookies[name]?.let {
                result.addAll(it.values)
            }
        }
        return result
    }

    @Synchronized
    override fun getCookie(url: HttpUrl): List<Cookie> {
        val result = arrayListOf<Cookie>()
        cookies[url.host]?.let {
            result.addAll(it.values)
        }
        return result
    }

    override fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean {
        if (!cookies.containsKey(url.host)) return false
        val urlCookies = cookies[url.host] ?: return false
        if (!urlCookies.contains(cookie.token)) return false

        val token = cookie.token
        urlCookies.remove(token)

        val editor = cookieSP.edit()
        if (cookieSP.contains(COOKIE_NAME_PREFIX + token)) {
            editor.remove(COOKIE_NAME_PREFIX + token)
        }
        editor.putString(url.host, TextUtils.join(",", urlCookies.keys))
        editor.apply()
        return true
    }

    override fun removeCookie(url: HttpUrl): Boolean {
        if (!cookies.containsKey(url.host)) return false
        val urlCookies = cookies.remove(url.host) ?: return false
        val editor = cookieSP.edit()
        for (token in urlCookies.keys) {
            if (cookieSP.contains(COOKIE_NAME_PREFIX + token)) {
                editor.remove(COOKIE_NAME_PREFIX + token)
            }
        }
        editor.remove(url.host)
        editor.apply()
        return true
    }

    override fun removeAllCookie(): Boolean {
        cookies.clear()
        val editor = cookieSP.edit()
        editor.clear()
        editor.apply()
        return true
    }

    private fun saveCookie(url: HttpUrl, cookie: Cookie, cookieToken: String) {
        if (!cookies.containsKey(url.host) || cookies[url.host] == null) {
            cookies[url.host] = ConcurrentHashMap<String, Cookie>()
        }
        cookies[url.host]!![cookieToken] = cookie
        val editor = cookieSP.edit()
        editor.putString(url.host, TextUtils.join(",", cookies[url.host]!!.keys))
        editor.putString(COOKIE_NAME_PREFIX + cookieToken, cookie.toHexString())
        editor.apply()
    }

    init {
        val prefsMap: Map<String, *> = cookieSP.all
        for (entry in prefsMap.entries) {
            if (entry.value != null && !entry.key.startsWith(COOKIE_NAME_PREFIX)) {
                val cookieNames = TextUtils.split(entry.value as String, ",")
                for (cookieName in cookieNames) {
                    val encodeCookieString =
                        cookieSP.getString(COOKIE_NAME_PREFIX + cookieName, null)
                    encodeCookieString?.let {
                        val cookie = it.toCookie()
                        if (cookie != null) {
                            if (!cookies.containsKey(entry.key)) {
                                cookies[entry.key] = ConcurrentHashMap<String, Cookie>()
                            }
                            cookies[entry.key]!![cookieName] = cookie
                        }
                    }
                }
            }
        }
    }
}