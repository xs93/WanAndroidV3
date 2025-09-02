package com.github.xs93.network.retorfit

import com.github.xs93.network.adapter.result.ResultCallAdapterFactory
import com.github.xs93.network.converter.HandleErrorConverterFactory
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
internal class RetrofitClient(
    val baseUrl: String,
    val retrofitBuildStrategy: RetrofitBuildStrategy
) {

    val retrofit: Retrofit by lazy {
        val builder = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(okHttpClient)

            addConverterFactory(HandleErrorConverterFactory.create())
            retrofitBuildStrategy.converterFactories()?.onEach {
                addConverterFactory(it)
            }

            addCallAdapterFactory(ResultCallAdapterFactory())
            retrofitBuildStrategy.callAdapterFactories()?.onEach {
                addCallAdapterFactory(it)
            }
        }
        builder.build()
    }

    val okHttpClient: OkHttpClient by lazy {
        retrofitBuildStrategy.okHttpClient()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}