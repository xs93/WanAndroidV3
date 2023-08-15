package com.github.xs93.network.progress.interfaces

/**
 *
 * 上传进度
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/15 17:52
 * @email 466911254@qq.com
 */
interface ProgressRequestListener {
    fun onRequestProgress(bytesWritten: Long, contentLength: Long, done: Boolean)
}