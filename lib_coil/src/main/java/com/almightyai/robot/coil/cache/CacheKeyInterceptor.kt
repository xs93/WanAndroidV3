package com.almightyai.robot.coil.cache

import coil.intercept.Interceptor
import coil.request.ImageResult

/**
 * Coil 缓存key 拦截器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/20 14:54
 * @email 466911254@qq.com
 */
class CacheKeyInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        val uri = request.data
        if (uri is String) {
            val cacheKey = CoilCacheKeyUtils.getKey(uri)
            val newRequest = request.newBuilder()
                .memoryCacheKey(cacheKey)
                .diskCacheKey(cacheKey)
                .build()

            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}