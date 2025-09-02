package com.github.xs93.network.annotation

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/7/23 13:43
 * @description timeOut 注解,可以动态修改超时时间,时间都是毫秒
 *
 * 拦截器实现详见：[com.github.xs93.network.interceptor.DynamicTimeoutInterceptor]
 *
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Timeout(val connectTimeout: Int, val readTimeout: Int, val writeTimeout: Int)
