package com.github.xs93.network.progress

import com.github.xs93.network.progress.interfaces.ProgressRequestListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Sink
import okio.buffer


/**
 * 上传进度对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/15 17:53
 * @email 466911254@qq.com
 */
class ProgressRequestBody(
    private val oldRequestBody: RequestBody,
    private var listener: ProgressRequestListener?,
) : RequestBody() {


    private var bufferedSink: BufferedSink? = null

    override fun contentType(): MediaType? {
        return oldRequestBody.contentType()
    }

    override fun contentLength(): Long {
        return oldRequestBody.contentLength()
    }

    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            bufferedSink = sink(sink).buffer()
        }
        // 必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink?.let {
            oldRequestBody.writeTo(it)
            it.flush()
        }
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            var bytesWritten: Long = 0L
            var contentLength: Long = 0L

            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    contentLength = contentLength()
                }
                bytesWritten += byteCount
                listener?.onRequestProgress(
                    bytesWritten,
                    contentLength,
                    bytesWritten == contentLength
                )
            }
        }
    }
}