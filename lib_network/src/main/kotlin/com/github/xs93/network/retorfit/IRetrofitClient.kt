package com.github.xs93.network.retorfit

import okhttp3.OkHttpClient

/**
 *
 *
 *
 * @author xushuai
 * @date   2022/9/2-14:24
 * @email  466911254@qq.com
 */
interface IRetrofitClient {

    /**
     * 生成server对象
     *
     * @param service 接口对象类
     * @return
     */
    fun <T> create(service: Class<T>): T

    fun getOkHttpClient(): OkHttpClient
}