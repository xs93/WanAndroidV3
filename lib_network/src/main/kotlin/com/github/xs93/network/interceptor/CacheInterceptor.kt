package com.github.xs93.network.interceptor

import android.content.Context
import com.github.xs93.network.annotation.Cache
import com.github.xs93.network.utils.CacheUtils
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation
import java.io.File

/**
 * 缓存拦截器配置
 *
 * @param context 上下文对象
 * @param key 数据加密key
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/3 10:07
 * @email 466911254@qq.com
 */
class CacheInterceptor(private val context: Context, private val key: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java) ?: return chain.proceed(request)
        val cacheConfig = invocation.method().getAnnotation(Cache::class.java) ?: return chain.proceed(request)

        val cacheKey = CacheUtils.getCacheKey(request)
        val cacheFile = File(CacheUtils.getCacheDir(context), cacheKey)

        val expiredTime = cacheConfig.expiredTime
        val cacheEnable = (System.currentTimeMillis() - cacheFile.lastModified()) < expiredTime
        if (cacheEnable && cacheFile.exists() && cacheFile.length() > 0) {
            val cache = CacheUtils.decryptContent(cacheFile.readText(), key)
            if (cache.isNotEmpty() && cache.startsWith("{") && cache.endsWith("}")) {
                return Response.Builder()
                    .code(200)
                    .body(cache.toResponseBody())
                    .request(request)
                    .message("from disk cache")
                    .protocol(Protocol.HTTP_2)
                    .build()
            }
        }

        val response = chain.proceed(request)
        val responseBody = response.body ?: return response
        val data = responseBody.bytes()
        val dataString = String(data)
        // 写入缓存
        if (response.code == 200) {
            // Json数据写入缓存
            cacheFile.writeText(CacheUtils.encryptContent(dataString, key))
        } else {
            cacheFile.writeText("")
        }
        return response.newBuilder()
            .body(data.toResponseBody(responseBody.contentType()))
            .build()
    }
}