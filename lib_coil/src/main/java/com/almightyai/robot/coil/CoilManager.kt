package com.almightyai.robot.coil

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.almightyai.robot.coil.progress.ProgressManager.coilProgressInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/4/11 11:35
 * @email 466911254@qq.com
 */
object CoilManager {

    private lateinit var mOkHttpClient: OkHttpClient

    fun init(context: Context, builder: ((ImageLoader.Builder) -> Unit)? = null) {
        mOkHttpClient = OkHttpClient().newBuilder()
            .coilProgressInterceptor()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        val imageLoaderBuilder = ImageLoader.Builder(context)
        imageLoaderBuilder.okHttpClient(mOkHttpClient)
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
                MemoryCache.Builder(context)
                    .maxSizePercent(0.3)
                    .build()
            }
            .networkCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
        builder?.invoke(imageLoaderBuilder)
        Coil.setImageLoader(imageLoaderBuilder.build())
    }
}