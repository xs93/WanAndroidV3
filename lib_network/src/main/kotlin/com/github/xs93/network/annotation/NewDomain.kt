package com.github.xs93.network.annotation

/**
 *
 * 使用新的BaseUrl,请求时会替换默认的baseUrl
 *
 * @author xushuai
 * @date   2022/9/2-13:54
 * @email  466911254@qq.com
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NewDomain(val domain: String)
