package com.github.xs93.network.interceptor

import com.github.xs93.network.annotation.NewDomain
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Invocation

/**
 *
 * 替换BaseUrl,see [com.berry.pinkberry.network.annotation.NewDomain]
 *
 *
 * @author xushuai
 * @date   2022/9/2-13:58
 * @email  466911254@qq.com
 */
class DomainInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(processRequest(chain.request()))
    }

    private fun processRequest(request: Request): Request {
        val invocation = request.tag(Invocation::class.java) ?: return request
        val newDomain = invocation.method().getAnnotation(NewDomain::class.java) ?: return request
        val domainValue = newDomain.domain
        val doMainUrl = domainValue.toHttpUrlOrNull() ?: return request
        val oldUlr = request.url
        val newUrl = oldUlr.newBuilder()
            .scheme(doMainUrl.scheme)
            .host(doMainUrl.host)
            .port(doMainUrl.port)
            .build()
        return request.newBuilder().url(newUrl).build()
    }
}