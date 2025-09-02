package com.github.xs93.coil.progress

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/4/11 11:44
 * @email 466911254@qq.com
 */
class ProgressResponseBody internal constructor(
    private val url: String,
    private val internalProgressListener: InternalProgressListener?,
    private val responseBody: ResponseBody
) : ResponseBody() {

    private var mBufferedSource: BufferedSource? = null


    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun source(): BufferedSource {
        if (mBufferedSource == null) {
            mBufferedSource = source(responseBody.source()).buffer()
        }
        return mBufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            // 当前读取字节数
            var totalBytesRead = 0L
            var lastTotalBytesRead: Long = 0L
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // 增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                if (internalProgressListener != null && lastTotalBytesRead != totalBytesRead) {
                    lastTotalBytesRead = totalBytesRead
                    mainHandler.post { internalProgressListener.onProgress(url, totalBytesRead, contentLength()) }
                }
                return bytesRead
            }
        }
    }


    internal interface InternalProgressListener {
        fun onProgress(url: String, bytesRead: Long, totalBytes: Long)
    }

    companion object {
        private val mainHandler = Handler(Looper.getMainLooper())
    }

}