package com.github.xs93.network.retorfit

import com.github.xs93.network.strategy.IRetrofitBuildStrategy
import okhttp3.OkHttpClient

/**
 * Retrofit对象构建接口
 *
 *
 * @author xushuai
 * @date   2022/9/2-14:24
 * @email  466911254@qq.com
 */
interface IRetrofitClient {

    val baseUrl: String

    val retrofitBuildStrategy: IRetrofitBuildStrategy
    fun getOkHttpClient(): OkHttpClient

    /**
     * 生成server对象
     *
     * @param service 接口对象类
     * @return
     */
    fun <T> create(service: Class<T>): T

}