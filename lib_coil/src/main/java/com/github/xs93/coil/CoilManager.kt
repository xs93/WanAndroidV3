package com.github.xs93.coil

import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.github.xs93.coil.progress.ProgressManager.coilProgressInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Coil 单利管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/4/11 11:35
 * @email 466911254@qq.com
 */
object CoilManager {
    fun init(context: Context, builder: ((ImageLoader.Builder) -> Unit)? = null) {
        val imageLoaderBuilder = ImageLoader.Builder(context)
        imageLoaderBuilder
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = {
                    OkHttpClient().newBuilder()
                        .coilProgressInterceptor()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build()
                }))
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                val cacheDir = context.externalCacheDir ?: context.cacheDir
                DiskCache.Builder()
                    .directory(cacheDir.resolve("coil_cache"))
                    .maxSizePercent(0.3)
                    .build()
            }
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.3)
                    .build()
            }
            .networkCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
        builder?.invoke(imageLoaderBuilder)
        SingletonImageLoader.setSafe {
            imageLoaderBuilder.build()
        }
    }
}