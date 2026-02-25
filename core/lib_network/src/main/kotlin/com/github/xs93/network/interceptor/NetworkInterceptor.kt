package com.github.xs93.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.github.xs93.network.exception.ERROR
import com.github.xs93.network.exception.NetworkException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 网络状态拦截器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/9 22:07
 * @email 466911254@qq.com
 */
class NetworkInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // 判断网络状态
        val isConnected: Boolean = runCatching {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            if (network != null) {
                val compatibility = connectivityManager.getNetworkCapabilities(network)
                if (compatibility != null) {
                    return@runCatching compatibility.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                }
            }
            return@runCatching false
        }.getOrDefault(false)

        if (isConnected) {
            return chain.proceed(chain.request())
        } else {
            throw NetworkException(ERROR.NETWORK_ERROR_NOT)
        }
    }
}