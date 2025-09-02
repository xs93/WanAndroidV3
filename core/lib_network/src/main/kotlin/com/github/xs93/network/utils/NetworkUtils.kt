package com.github.xs93.network.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/27 9:50
 * @email 466911254@qq.com
 */
object NetworkUtils {

    private val mOkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    suspend fun checkGoogleOk(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("https://www.google.com/")
                    .build()
                val response = mOkHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d("NetworkUtils", "VPN 连接google 成功")
                    return@withContext true
                }
                Log.d("NetworkUtils", "VPN 连接google 失败")
                false
            } catch (e: Exception) {
                Log.e("NetworkUtils", "VPN 连接google 失败:$e")
                false
            }
        }
    }


    suspend fun requestIpInfo() {
        withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url("https://ipinfo.io").build()
                val response = client.newCall(request).execute()
                val msg: String = if (response.isSuccessful) {
                    val bodyString = response.body?.string() ?: ""
                    if (bodyString.contains("\"userIp\"")) {
                        val index = bodyString.indexOf("\"userIp\"")
                        bodyString.substring(index, index + 30)
                    } else {
                        ""
                    }
                } else {
                    "请求错误"
                }
                Log.d("NetworkUtils", msg)
            } catch (e: Exception) {
                Log.d("NetworkUtils", "requestIpInfo error:$e")
            }
        }
    }
}