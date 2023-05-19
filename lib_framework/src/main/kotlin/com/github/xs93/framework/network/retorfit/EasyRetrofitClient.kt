package com.github.xs93.framework.network.retorfit

import com.github.xs93.framework.network.EasyRetrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 默认实现的retrofit客户端
 *
 *
 * @author xushuai
 * @date   2022/9/2-14:24
 * @email  466911254@qq.com
 */
object EasyRetrofitClient : IRetrofitClient {
    private val retrofit by lazy {
        val strategy = EasyRetrofit.getStrategy()
        val builder = Retrofit.Builder().apply {
            baseUrl(EasyRetrofit.getBaseUrl())
            client(mOkHttpClient)
            strategy.converterFactory()?.apply {
                addConverterFactory(this)
            }
            strategy.callAdapterFactory()?.apply {
                addCallAdapterFactory(this)
            }
        }
        builder.build()
    }

    private val mOkHttpClient by lazy {
        EasyRetrofit.getStrategy().okHttpClient()
    }

    override fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    override fun getOkHttpClient(): OkHttpClient {
        return mOkHttpClient
    }
}