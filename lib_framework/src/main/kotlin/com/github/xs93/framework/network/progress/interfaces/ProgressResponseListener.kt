package com.github.xs93.framework.network.progress.interfaces

/**
 * 下载进度
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/15 17:52
 * @email 466911254@qq.com
 */
interface ProgressResponseListener {
    fun onResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean)
}