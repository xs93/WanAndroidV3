package com.github.xs93.network.annotation

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 14:01
 * @description DynamicBaseUrl,可以注解在类和方法上,替换网络请求时的baseUrl
 *
 *  当 baseUrl 不为null时，使用 baseUrl替换旧的 baseUrl,
 *  当 baseUrlKey 不为null时,使用 baseUrlKey对应的值旧的 baseUrl,
 *  当 baseUrlKey 和 baseUrl 都为null时,使用全局配置的 globalBaseUrl
 *
 *  当需要在运行时动态修改baseUrl时,可以使用此注解配置key,请求时拦截器会根据key动态替换请求的BaseUrl
 *
 *  当方法使用@Url 注解时,@DynamicBaseUrl注解无效
 *
 *  @param baseUrlKey 动态baseUrl的key
 *  @param baseUrl 静态baseUrl
 *
 *   url获取优先级
 *   url>methodBaseUrl>methodBaseUrlKey>classBaseUrl>classBaseUrlKey>globalBaseUrl
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class DynamicBaseUrl(val baseUrlKey: String = "", val baseUrl: String = "")
