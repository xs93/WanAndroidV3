package com.github.xs93.network.interceptor

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.network.annotation.DynamicBaseUrl
import com.github.xs93.network.model.UrlsConfig
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
 * @description 动态修改接口地址拦截器
 *
 * 根据 [DynamicBaseUrl] 注解的配置动态修改接口地址
 */
class DynamicBaseUrlInterceptor() : Interceptor {

    private val urlsConfigCache = ConcurrentHashMap<Method, UrlsConfig>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val method = invocation?.method() ?: return chain.proceed(request)

        val (methodUrl, methodUrlKey, classUrl, classUrlKey, urlAnnotationIndex) =
            urlsConfigCache.getOrPut(method) {
                val methodAnnotation = method.getAnnotation(DynamicBaseUrl::class.java)
                val methodBaseUrl = methodAnnotation?.baseUrl?.takeIfValidUrl()
                val methodBaseUrlKey = methodAnnotation?.baseUrlKey?.takeIfNotEmpty()
                val classAnnotation =
                    method.declaringClass.getAnnotation(DynamicBaseUrl::class.java)
                val classBaseUrl = classAnnotation?.baseUrl?.takeIfValidUrl()
                val classBaseUrlKey = classAnnotation?.baseUrlKey?.takeIfNotEmpty()
                val urlAnnotationIndex =
                    method.parameterAnnotations.indexOfFirst { annotations -> annotations.any { it is Url } }
                UrlsConfig(
                    methodBaseUrl,
                    methodBaseUrlKey,
                    classBaseUrl,
                    classBaseUrlKey,
                    urlAnnotationIndex
                )
            }

        invocation.arguments().getOrNull(urlAnnotationIndex)?.toString()?.takeIfValidUrl()
            ?.run { return chain.proceed(request) }

        val methodBaseUrl = methodUrl?.takeIfNotEmpty()
            ?: methodUrlKey?.let { EasyRetrofit.getDynamicBaseUrlByKey(it) }?.takeIfValidUrl()

        val classBaseUrl = classUrl?.takeIfNotEmpty()
            ?: classUrlKey?.let { EasyRetrofit.getDynamicBaseUrlByKey(it) }?.takeIfValidUrl()

        val newBaseUrl = (methodBaseUrl ?: classBaseUrl)?.toHttpUrlOrNull()
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

    private fun String.takeIfNotEmpty() = takeIf { it.isNotBlank() }

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