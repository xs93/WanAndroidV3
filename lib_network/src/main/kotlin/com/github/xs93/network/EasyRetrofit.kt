package com.github.xs93.network

import android.annotation.SuppressLint
import android.content.Context
import com.github.xs93.network.exception.IErrorHandler
import com.github.xs93.network.retorfit.RetrofitClient
import com.github.xs93.network.strategy.RetrofitBuildStrategy
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

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

    /**
     * 全局错误处理器
     */
    val errorHandlers = CopyOnWriteArrayList<IErrorHandler>()

    fun init(context: Context) {
        mApp = context.applicationContext
    }

    fun getApp(): Context {
        return mApp ?: throw IllegalStateException("please call EasyRetrofit.init() method")
    }

    fun createRetrofit(baseUrl: String, strategy: RetrofitBuildStrategy? = null) {
        retrofitClient = RetrofitClient(baseUrl, strategy ?: RetrofitBuildStrategy())
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

    /**
     * 添加错误处理
     */
    fun addErrorHandler(handler: IErrorHandler) {
        errorHandlers.add(handler)
    }

    /**
     * 移除错误处理
     */
    fun removeErrorHandler(handler: IErrorHandler) {
        errorHandlers.remove(handler)
    }

    fun getRetrofit(): Retrofit? {
        return retrofitClient?.retrofit
    }

    fun getOkHttpClient(): OkHttpClient? {
        return retrofitClient?.okHttpClient
    }

    private fun checkRetrofitClient() {
        if (retrofitClient == null) {
            throw IllegalStateException("please call EasyRetrofit.addRetrofitClient() method")
        }
    }
}