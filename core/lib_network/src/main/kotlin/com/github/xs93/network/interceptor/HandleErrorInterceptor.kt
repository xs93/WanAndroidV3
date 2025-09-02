package com.github.xs93.network.interceptor

import com.github.xs93.network.exception.ExceptionHandler
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/7 10:56
 * @description 处理请求错误和服务器错误的拦截器
 *
 */
class HandleErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            if (response.isSuccessful) {
                return response
            } else {
                throw ExceptionHandler.handleErrorResponse(response)
            }
        } catch (e: IOException) {
            throw ExceptionHandler.handleNetworkException(e)
        }
    }
}
