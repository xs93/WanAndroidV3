package com.github.xs93.framework.network

import android.annotation.SuppressLint
import android.content.Context
import com.github.xs93.framework.network.retorfit.EasyRetrofitClient
import com.github.xs93.framework.network.retorfit.IRetrofitClient
import com.github.xs93.framework.network.strategy.EasyRetrofitBuildStrategy
import com.github.xs93.framework.network.strategy.IRetrofitBuildStrategy

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
    private var mBaseUrl: String? = null
    private var mStrategy: IRetrofitBuildStrategy? = null
    private var mOpenOkHttpProfiler: Boolean = false

    fun init(
        context: Context,
        baseUrl: String,
        strategy: IRetrofitBuildStrategy? = EasyRetrofitBuildStrategy(),
        openOkHttpProfiler: Boolean = false,
    ) {
        mApp = context.applicationContext
        mBaseUrl = baseUrl
        mStrategy = strategy
        mOpenOkHttpProfiler = openOkHttpProfiler
    }

    fun getApp(): Context {
        return mApp ?: throw IllegalStateException("please call HttpManager.init() method")
    }

    fun getBaseUrl(): String {
        return mBaseUrl ?: throw IllegalStateException("please call HttpManager.init() method")
    }

    fun getStrategy(): IRetrofitBuildStrategy {
        return mStrategy ?: EasyRetrofitBuildStrategy().also {
            mStrategy = it
        }
    }

    fun isOpenOkHttpProfiler(): Boolean {
        return mOpenOkHttpProfiler
    }

    fun <T> create(retrofitClient: IRetrofitClient = EasyRetrofitClient, service: Class<T>): T {
        return retrofitClient.create(service)
    }
}