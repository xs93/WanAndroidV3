package com.github.xs93.network.strategy

import com.github.xs93.network.adapter.result.ResultCallAdapterFactory
import com.github.xs93.network.moshi.adapter.BooleanAdapter
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.ktx.isDebug
import com.squareup.moshi.Moshi
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 *
 * 默认Retrofit策略模式
 *
 * @author xushuai
 * @date   2022/9/2-14:12
 * @email  466911254@qq.com
 */
class EasyRetrofitBuildStrategy : IRetrofitBuildStrategy {

    override fun converterFactory(): List<Converter.Factory> {
        val moshi = Moshi.Builder()
            .add(BooleanAdapter())
            .build()
        return arrayListOf(
            ScalarsConverterFactory.create(),
            MoshiConverterFactory.create(moshi)
        )
    }

    override fun callAdapterFactory(): List<CallAdapter.Factory> {
        return arrayListOf(ResultCallAdapterFactory())
    }

    override fun openOkHttpProfiler(): Boolean {
        return AppInject.getApp().isDebug
    }

    override fun isMultipleBaseUrlEnable(): Boolean {
        return false
    }

    override fun getDynamicBaseUrlByKey(key: String): String? {
        return null
    }

    override fun setDynamicBaseUrlByKey(key: String, baseUrl: String) {

    }

    override fun getGlobalBaseUrl(): String? {
        return null
    }

    override fun setGlobalBaseUrl(baseUrl: String) {
    }
}