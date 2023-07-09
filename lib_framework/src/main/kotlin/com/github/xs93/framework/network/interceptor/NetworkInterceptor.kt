package com.github.xs93.framework.network.interceptor

import com.github.xs93.framework.core.utils.AppInject
import com.github.xs93.framework.network.exception.ERROR
import com.github.xs93.framework.network.exception.NoNetworkException
import com.github.xs93.framework.network.utils.NetworkUtils
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
        if (NetworkUtils.isConnected(AppInject.getApp())) {
            return chain.proceed(chain.request())
        } else {
            throw NoNetworkException(ERROR.NETWORK_ERROR)
        }
    }
}