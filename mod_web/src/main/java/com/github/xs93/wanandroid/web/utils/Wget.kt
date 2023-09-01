package com.github.xs93.wanandroid.web.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/3/15
 */
object Wget {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    fun get(url: String): String {
        val request = Request.Builder()
            .url(url)
            .header(
                "user-agent",
                "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3887.7 Mobile Safari/537.36"
            )
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }
}