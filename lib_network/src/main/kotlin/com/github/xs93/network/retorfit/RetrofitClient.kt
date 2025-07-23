package com.github.xs93.network.retorfit

import com.github.xs93.network.strategy.RetrofitBuildStrategy
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
class RetrofitClient(val baseUrl: String, val retrofitBuildStrategy: RetrofitBuildStrategy) {
    private val retrofit by lazy {
        val builder = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(mOkHttpClient)
            retrofitBuildStrategy.converterFactories()?.onEach {
                addConverterFactory(it)
            }
            retrofitBuildStrategy.callAdapterFactories()?.onEach {
                addCallAdapterFactory(it)
            }
        }
        builder.build()
    }

    private val mOkHttpClient by lazy {
        retrofitBuildStrategy.okHttpClient()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun getOkHttpClient(): OkHttpClient {
        return mOkHttpClient
    }
}