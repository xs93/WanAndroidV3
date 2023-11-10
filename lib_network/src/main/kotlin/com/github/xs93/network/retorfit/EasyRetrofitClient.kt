package com.github.xs93.network.retorfit

import com.github.xs93.network.strategy.IRetrofitBuildStrategy
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
class EasyRetrofitClient(override val baseUrl: String, override val retrofitBuildStrategy: IRetrofitBuildStrategy) :
    IRetrofitClient {

    private val retrofit by lazy {
        val builder = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            client(mOkHttpClient)
            retrofitBuildStrategy.converterFactory()?.onEach {
                addConverterFactory(it)
            }
            retrofitBuildStrategy.callAdapterFactory()?.apply {
                addCallAdapterFactory(this)
            }
        }
        builder.build()
    }

    private val mOkHttpClient by lazy {
        retrofitBuildStrategy.okHttpClient()
    }

    override fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    override fun getOkHttpClient(): OkHttpClient {
        return mOkHttpClient
    }
}