package com.github.xs93.coil.progress

import coil3.request.ImageRequest
import okhttp3.OkHttpClient
import java.util.concurrent.ConcurrentHashMap

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/4/11 11:41
 * @email 466911254@qq.com
 */
object ProgressManager {


    private val mListenerMap = ConcurrentHashMap<String, OnProgressListener>()


    private val mListener = object : ProgressResponseBody.InternalProgressListener {
        override fun onProgress(url: String, bytesRead: Long, totalBytes: Long) {
            getProgressListener(url)?.let {
                val percentage = (bytesRead * 1f / totalBytes * 100f).toInt()
                val isComplete = percentage >= 100
                it.invoke(isComplete, percentage, bytesRead, totalBytes)
                if (isComplete) {
                    removeListener(url)
                }
            }
        }
    }


    fun OkHttpClient.Builder.coilProgressInterceptor(): OkHttpClient.Builder =
        addNetworkInterceptor {
            val request = it.request()
            val response = it.proceed(request)
            response.newBuilder().run {
                val body = response.body
                if (body != null) {
                    this.body(ProgressResponseBody(request.url.toString(), mListener, body))
                }
                build()
            }
        }


    fun ImageRequest.Builder.progress(url: String, listener: OnProgressListener) = apply {
        addListener(url, listener)
    }

    private fun addListener(url: String, listener: OnProgressListener) {
        if (url.isNotBlank()) {
            mListenerMap[url] = listener
        }
    }

    fun removeListener(url: String) {
        if (url.isNotBlank()) {
            mListenerMap.remove(url)
        }
    }

    fun getProgressListener(url: String?): OnProgressListener {
        return if (url.isNullOrBlank() || mListenerMap.isEmpty) {
            null
        } else {
            mListenerMap[url]
        }
    }
}