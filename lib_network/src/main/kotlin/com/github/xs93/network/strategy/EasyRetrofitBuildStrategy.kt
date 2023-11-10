package com.github.xs93.network.strategy

import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory

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
        return arrayListOf(MoshiConverterFactory.create())
    }

    override fun callAdapterFactory(): CallAdapter.Factory? {
        return null
    }

    override fun openOkHttpProfiler(): Boolean {
        return true
    }
}