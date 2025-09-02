package com.github.xs93.network.interceptor

import com.github.xs93.network.annotation.Timeout
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import java.util.concurrent.TimeUnit

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/7/23 13:45
 * @description 动态配置超时时间拦截器,
 *
 * 具体使用查看注解 [Timeout]
 *
 */
class DynamicTimeoutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(Timeout::class.java)
            ?.let {
                return chain.withConnectTimeout(it.connectTimeout, TimeUnit.MILLISECONDS)
                    .withReadTimeout(it.readTimeout, TimeUnit.MILLISECONDS)
                    .withWriteTimeout(it.writeTimeout, TimeUnit.MILLISECONDS)
                    .proceed(request)
            }
        return chain.proceed(request)
    }
}