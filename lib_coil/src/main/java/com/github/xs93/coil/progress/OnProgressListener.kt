package com.github.xs93.coil.progress

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/4/11 11:40
 * @email 466911254@qq.com
 */


typealias OnProgressListener = ((isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) -> Unit)?