package com.github.xs93.network

import android.annotation.SuppressLint
import android.content.Context
import com.github.xs93.network.exception.ExceptionHandler
import com.github.xs93.network.retorfit.RetrofitClient
import com.github.xs93.network.strategy.RetrofitBuildStrategy

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

    private fun checkRetrofitClient() {
        if (retrofitClient == null) {
            throw IllegalStateException("please call EasyRetrofit.addRetrofitClient() method")
        }
    }
}