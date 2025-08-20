package com.github.xs93.network.okhttp

import okhttp3.OkHttpClient

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/20 15:12
 * @description OkHttpClient管理类，全局App使用一个OKHttp Client,其他需要可以通过 baseOkHttpClient.newBuilder自定义需求,这样全局共享连接池
 *
 */
object OkHttpClientManager {

    private val baseOkHttpClient: OkHttpClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { createOkHttpClient() }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    fun getBaseClient(): OkHttpClient {
        return baseOkHttpClient
    }
}