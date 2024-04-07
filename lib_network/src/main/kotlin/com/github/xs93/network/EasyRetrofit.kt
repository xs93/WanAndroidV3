package com.github.xs93.network

import android.annotation.SuppressLint
import android.content.Context
import com.github.xs93.network.exception.ExceptionHandler
import com.github.xs93.network.retorfit.EasyRetrofitClient
import com.github.xs93.network.retorfit.IRetrofitClient
import com.github.xs93.network.strategy.EasyRetrofitBuildStrategy
import com.github.xs93.network.strategy.IRetrofitBuildStrategy

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
    private val strategyMap = HashMap<String, IRetrofitClient>()

    fun init(context: Context, safeRequestApiErrorHandler: ((Throwable) -> Unit)? = null) {
        mApp = context.applicationContext
        ExceptionHandler.safeRequestApiErrorHandler = safeRequestApiErrorHandler
    }

    fun getApp(): Context {
        return mApp ?: throw IllegalStateException("please call EasyRetrofit.init() method")
    }

    fun addRetrofitClient(baseUrl: String, retrofitClient: IRetrofitClient) {
        strategyMap[baseUrl] = retrofitClient
    }

    fun addRetrofitClient(baseUrl: String, retrofitBuildStrategy: IRetrofitBuildStrategy) {
        strategyMap[baseUrl] = EasyRetrofitClient(baseUrl, retrofitBuildStrategy)
    }

    fun addRetrofitClient(baseUrl: String) {
        strategyMap[baseUrl] = EasyRetrofitClient(baseUrl, EasyRetrofitBuildStrategy())
    }

    fun getRetrofitClient(baseUrl: String): IRetrofitClient {
        return strategyMap[baseUrl] ?: throw IllegalStateException("please call EasyRetrofit.addRetrofitClient() method")
    }

    fun <T> create(baseUrl: String, service: Class<T>): T {
        val retrofitClient =
            strategyMap[baseUrl] ?: throw IllegalStateException("please call EasyRetrofit.addRetrofitClient() method")
        return retrofitClient.create(service)
    }
}