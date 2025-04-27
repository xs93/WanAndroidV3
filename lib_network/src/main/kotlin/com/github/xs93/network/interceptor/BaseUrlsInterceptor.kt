package com.github.xs93.network.interceptor

import com.github.xs93.network.annotation.BaseUrl
import com.github.xs93.network.model.UrlsConfig
import com.github.xs93.network.strategy.IRetrofitBuildStrategy
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.http.Url
import java.lang.reflect.Method
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 14:11
 * @description
 *
 */
class BaseUrlsInterceptor(private val strategy: IRetrofitBuildStrategy) : Interceptor {

    private val urlsConfigCache = ConcurrentHashMap<Method, UrlsConfig>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!strategy.isMultipleBaseUrlEnable()) return chain.proceed(request)
        val invocation = request.tag(Invocation::class.java)
        val method = invocation?.method() ?: return chain.proceed(request)

        val (apiBaseUrl, methodUrlKey, clazzUrlKey, urlAnnotationIndex) =
            urlsConfigCache.getOrPut(method) {
                val apiBaseUrl = method.getAnnotation(BaseUrl::class.java)?.value?.takeIfValidUrl()
                    ?: method.declaringClass.getAnnotation(BaseUrl::class.java)?.value?.takeIfValidUrl()
                val methodUrlKey = method.getAnnotation(BaseUrl::class.java)?.key?.takeIfNotEmpty()
                val clazzUrlKey =
                    method.declaringClass.getAnnotation(BaseUrl::class.java)?.key?.takeIfNotEmpty()
                val urlAnnotationIndex =
                    method.parameterAnnotations.indexOfFirst { annotations -> annotations.any { it is Url } }
                UrlsConfig(apiBaseUrl, methodUrlKey, clazzUrlKey, urlAnnotationIndex)
            }

        invocation.arguments().getOrNull(urlAnnotationIndex)?.toString()?.takeIfValidUrl()
            ?.run { return chain.proceed(request) }

        val dynamicBaseUrl =
            methodUrlKey?.let { strategy.getDynamicBaseUrlByKey(it) }?.takeIfValidUrl()
                ?: clazzUrlKey?.let { strategy.getDynamicBaseUrlByKey(it) }?.takeIfValidUrl()

        val newBaseUrl =
            (dynamicBaseUrl ?: apiBaseUrl ?: strategy.getGlobalBaseUrl())?.toHttpUrlOrNull()
                ?: return chain.proceed(request)

        val newFullUrl = request.url.newBuilder()
            .scheme(newBaseUrl.scheme)
            .host(newBaseUrl.host)
            .port(newBaseUrl.port)
            .apply {
                (0 until request.url.pathSize).forEach { _ ->
                    removePathSegment(0)
                }
                (newBaseUrl.encodedPathSegments + request.url.encodedPathSegments).forEach {
                    addPathSegment(it)
                }
            }
            .build()
        return chain.proceed(request.newBuilder().url(newFullUrl).build())
    }

    private fun String.takeIfNotEmpty() = takeIf { it.isNotEmpty() }

    private fun String.takeIfValidUrl() = takeIf {
        try {
            val url = URL(this)
            url.protocol == "http" || url.protocol == "https"
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            false
        }
    }
}