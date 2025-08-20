package com.github.xs93.coil

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.bitmapConfig
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.github.xs93.coil.progress.ProgressManager.coilProgressInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/20
 * @description Coil 单例管理类
 *
 */
@Suppress("unused")
object CoilManager {

    private var imageLoader: ImageLoader? = null

    fun init(
        context: Context,
        okHttpClient: OkHttpClient,
        builder: ((ImageLoader.Builder) -> Unit)? = null
    ) {
        val imageLoaderBuilder = ImageLoader.Builder(context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache(initMemoryCache(context))
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache(initDiskCache(context))
            .networkCachePolicy(CachePolicy.ENABLED)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = {
                    okHttpClient.newBuilder()
                        .coilProgressInterceptor()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build()
                }))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                add(SvgDecoder.Factory())
            }
            .crossfade(true)
        builder?.invoke(imageLoaderBuilder)
        imageLoader = imageLoaderBuilder.build()
        SingletonImageLoader.setSafe { imageLoader!! }
    }

    fun getImageLoader(): ImageLoader? {
        checkInit()
        return imageLoader
    }

    private fun checkInit() {
        if (imageLoader == null) {
            throw IllegalStateException("CoilManager not init")
        }
    }

    private fun initMemoryCache(context: Context): MemoryCache {
        val memoryCache = MemoryCache.Builder()
            .maxSizePercent(context, 0.3)
            .build()
        return memoryCache
    }

    private fun initDiskCache(context: Context): DiskCache {
        val cacheDir = context.externalCacheDir ?: context.cacheDir
        val diskCache = DiskCache.Builder()
            .directory(cacheDir.resolve("coil_disk_cache"))
            .maxSizePercent(0.3)
            .build()
        return diskCache
    }
}