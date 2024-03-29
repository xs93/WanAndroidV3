package com.github.xs93.network.strategy

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

    override fun callAdapterFactory(): CallAdapter.Factory? {
        return null
    }

    override fun openOkHttpProfiler(): Boolean {
        return AppInject.getApp().isDebug
    }
}