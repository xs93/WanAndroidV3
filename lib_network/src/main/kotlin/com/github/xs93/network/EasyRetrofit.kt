package com.github.xs93.network

import android.annotation.SuppressLint
import android.content.Context
import com.github.xs93.network.exception.ExceptionHandler
import com.github.xs93.network.retorfit.RetrofitClient
import com.github.xs93.network.strategy.RetrofitBuildStrategy
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * EasyRetrofit管理类
 *
 * @author xushuai
 * @date   2022/9/2-14:10
 * @email  466911254@qq.com
 */
@SuppressLint("StaticFieldLeak")
object EasyRetrofit {

    private var mApp: Context? = null
    private var retrofitClient: RetrofitClient? = null

    /**
     * 动态baseUrl
     * 使用线程安全的 ConcurrentHashMap，防止线程安全问题
     */
    private val dynamicBaseUrls = ConcurrentHashMap<String, String>()

    fun init(
        context: Context,
        safeRequestApiErrorHandler: ((Throwable) -> Unit)? = null
    ) {
        mApp = context.applicationContext
        ExceptionHandler.safeRequestApiErrorHandler = safeRequestApiErrorHandler
    }

    fun getApp(): Context {
        return mApp ?: throw IllegalStateException("please call EasyRetrofit.init() method")
    }

    fun createRetrofit(baseUrl: String, strategy: RetrofitBuildStrategy) {
        retrofitClient = RetrofitClient(baseUrl, strategy)
    }

    fun <T> create(service: Class<T>): T {
        checkRetrofitClient()
        return retrofitClient!!.create(service)
    }


    /**
     * 根据key获取baseUrl
     */
    fun getDynamicBaseUrl(key: String): String? {
        return dynamicBaseUrls[key]
    }

    /**
     * 设置baseUrl
     */
    fun putDynamicBaseUrl(key: String, baseUrl: String) {
        dynamicBaseUrls[key] = baseUrl
    }

    /**
     * 移除对应key的baseUrl
     */
    fun removeDynamicBaseUrl(key: String) {
        dynamicBaseUrls.remove(key)
    }

    private fun checkRetrofitClient() {
        if (retrofitClient == null) {
            throw IllegalStateException("please call EasyRetrofit.addRetrofitClient() method")
        }
    }
}