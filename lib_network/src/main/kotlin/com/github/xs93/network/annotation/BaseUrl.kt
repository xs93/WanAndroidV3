package com.github.xs93.network.annotation

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 14:01
 * @description BaseUrl注解,可以注解在类和方法上,替换网络请求时的baseUrl
 *  当Value 不为null时，使用Value的值替换baseUrl,
 *  当key 不为null时,使用key对应的值替换baseUrl
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class BaseUrl(val key: String = "", val value: String = "")
