package com.github.xs93.network.strategy

import android.content.Context
import com.github.xs93.network.EasyRetrofit
import com.github.xs93.network.cookie.CookieJarManager
import com.github.xs93.network.cookie.SharedPreferencesCookieStore
import com.github.xs93.network.interceptor.CacheInterceptor
import com.github.xs93.network.interceptor.DynamicBaseUrlInterceptor
import com.github.xs93.network.interceptor.DynamicTimeoutInterceptor
import com.github.xs93.network.interceptor.HandleErrorInterceptor
import com.github.xs93.network.interceptor.NetworkInterceptor
import com.github.xs93.network.moshi.adapter.BooleanAdapter
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.crypt.AESCrypt
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 构建一个Retrofit client需要的客户端
 *
 *
 * @author xushuai
 * @date   2022/9/2-14:11
 * @email  466911254@qq.com
 */
open class RetrofitBuildStrategy {

    /** 构建Retrofit 的OkHttpClient */
    fun okHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            connectTimeout(getTimeout(), TimeUnit.SECONDS)
            writeTimeout(getTimeout(), TimeUnit.SECONDS)
            readTimeout(getTimeout(), TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            cache(getCache())
            cookieJar(getCookieJar())
            addInterceptor(HandleErrorInterceptor())
            addInterceptor(NetworkInterceptor())
            addInterceptor(DynamicTimeoutInterceptor())
            addInterceptor(DynamicBaseUrlInterceptor())
            addInterceptor(
                CacheInterceptor(
                    AppInject.getApp(),
                    getCacheEncryptKey(),
                    getCacheEncryptIv()
                )
            )
            getInterceptors().let {
                for (interceptor in it) {
                    addInterceptor(interceptor)
                }
            }

            getNetworkInterceptors().let {
                for (interceptor in it) {
                    addNetworkInterceptor(interceptor)
                }
            }
            try {
                val clazz = Class.forName("com.localebro.okhttpprofiler.OkHttpProfilerInterceptor")
                if (clazz != null) {
                    val interceptor = clazz.getDeclaredConstructor().newInstance() as Interceptor
                    addInterceptor(interceptor)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            customOkHttpClient(this)
        }
        return builder.build()
    }


    fun getTimeout(): Long = 30

    /**
     * 定制一些okHttpClient的配置
     */
    open fun customOkHttpClient(builder: OkHttpClient.Builder) {

    }

    open fun getCache(): Cache {
        val context: Context = EasyRetrofit.getApp()
        val cacheFile = File(context.cacheDir, "OkHttpCache")
        if (!cacheFile.exists()) {
            cacheFile.mkdirs()
        }
        return Cache(cacheFile, 50L * 1024L * 1024L)
    }

    open fun getCookieJar(): CookieJar {
        val context: Context = EasyRetrofit.getApp()
        return CookieJarManager(SharedPreferencesCookieStore(context))
    }

    open fun getInterceptors(): List<Interceptor> {
        return emptyList()
    }

    open fun getNetworkInterceptors(): List<Interceptor> {
        return emptyList()
    }

    open fun getCacheEncryptKey(): String {
        val context: Context = EasyRetrofit.getApp()
        val sp = context.getSharedPreferences("network_cache_pro", Context.MODE_PRIVATE)
        var encryptKey = sp.getString("key", "")
        if (encryptKey.isNullOrBlank()) {
            encryptKey = AESCrypt.generateKeyString(16)
            sp.edit().putString("key", encryptKey).apply()
        }
        return encryptKey
    }

    open fun getCacheEncryptIv(): String {
        val context: Context = EasyRetrofit.getApp()
        val sp = context.getSharedPreferences("network_cache_pro", Context.MODE_PRIVATE)
        var iv = sp.getString("iv", "")
        if (iv.isNullOrBlank()) {
            iv = AESCrypt.generateIvString()
            sp.edit().putString("iv", iv).apply()
        }
        return iv
    }

    /** 构建Retrofit 的Converter.Factory */
    open fun converterFactories(): List<Converter.Factory>? {
        val moshi = Moshi.Builder()
            .add(BooleanAdapter())
            .build()
        return arrayListOf(
            ScalarsConverterFactory.create(),
            MoshiConverterFactory.create(moshi)
        )
    }

    /** 构建Retrofit 的CallAdapter.Factory */
    open fun callAdapterFactories(): List<CallAdapter.Factory>? {
        return null
    }
}