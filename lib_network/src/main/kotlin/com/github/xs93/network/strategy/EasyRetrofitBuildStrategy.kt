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

    override fun converterFactory(): Converter.Factory? {
        return MoshiConverterFactory.create()
    }

    override fun callAdapterFactory(): CallAdapter.Factory? {
        return null
    }

    override fun getCacheKey(): String {
        return "3b82acbd4cb9a82c36cc3f874bad3e5e80f9a138bb178a9c4cab48e211d0db79"
    }
}