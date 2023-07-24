package com.github.xs93.framework.network.strategy

import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 *
 *
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
        return "adjkasjasfhjkaskfa"
    }
}