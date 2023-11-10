package com.github.xs93.network.progress

import com.github.xs93.network.progress.interfaces.ProgressResponseListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer


/**
 * 下载数据进度
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/16 17:47
 * @email 466911254@qq.com
 */
class ProgressResponseBody(
    private val oldResponseBody: ResponseBody,
    private val listener: ProgressResponseListener?,
) : ResponseBody() {

    private var mBufferedSource: BufferedSource? = null

    override fun contentLength(): Long {
        return oldResponseBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return oldResponseBody.contentType()
    }

    override fun source(): BufferedSource {
        if (mBufferedSource == null) {
            mBufferedSource = source(oldResponseBody.source()).buffer()
        }
        return mBufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            //当前读取字节数
            var totalBytesRead = 0L
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // 增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                // 回调，如果contentLength()不知道长度，会返回-1
                listener?.onResponseProgress(
                    totalBytesRead,
                    oldResponseBody.contentLength(),
                    bytesRead == -1L
                )
                return bytesRead
            }
        }
    }
}