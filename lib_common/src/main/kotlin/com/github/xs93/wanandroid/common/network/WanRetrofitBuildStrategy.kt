package com.github.xs93.wanandroid.common.network

import com.github.xs93.network.adapter.result.ResultCallAdapterFactory
import com.github.xs93.network.moshi.adapter.BooleanAdapter
import com.github.xs93.network.strategy.IRetrofitBuildStrategy
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.ktx.isDebug
import com.squareup.moshi.Moshi
import okhttp3.CookieJar
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

/**
 * retrofit 构建策略
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 13:49
 * @email 466911254@qq.com
 */
class WanRetrofitBuildStrategy @Inject constructor(private val cookieJar: CookieJar) : IRetrofitBuildStrategy {
    override fun converterFactory(): List<Converter.Factory> {
        val moshi = Moshi.Builder()
            .add(BooleanAdapter())
            .build()
        return arrayListOf(
            ScalarsConverterFactory.create(),
            MoshiConverterFactory.create(moshi)
        )
    }

    override fun callAdapterFactory(): List<CallAdapter.Factory>? {
        return arrayListOf(ResultCallAdapterFactory())
    }

    override fun openOkHttpProfiler(): Boolean {
        return AppInject.getApp().isDebug
    }

    override fun getCookieJar(): CookieJar {
        return cookieJar
    }
}