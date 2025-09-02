package com.github.xs93.network.interceptor

import com.github.xs93.network.exception.ERROR
import com.github.xs93.network.exception.NetworkException
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.net.isNetworkConnected
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
class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val app = AppInject.getApp()
        if (app.isNetworkConnected()) {
            return chain.proceed(chain.request())
        } else {
            throw NetworkException(ERROR.NETWORK_ERROR_NOT)
        }
    }
}