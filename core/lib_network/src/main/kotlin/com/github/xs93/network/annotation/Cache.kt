package com.github.xs93.network.annotation

/**
 * 缓存拦截器配置注解
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/3 10:03
 * @email 466911254@qq.com
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Cache(val expiredTime: Long)