package com.github.xs93.utils.ktx

import java.io.Closeable
import java.io.IOException

/**
 * 关闭流相关扩展函数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/6 11:30
 * @email 466911254@qq.com
 */


fun Closeable?.safeClose(quietly: Boolean = false) {
    try {
        this?.close()
    } catch (e: IOException) {
        if (!quietly) {
            e.printStackTrace()
        }
    }
}

fun closeIO(vararg closeables: Closeable?) {
    closeables.forEach {
        it.safeClose()
    }
}

fun closeIOQuietly(vararg closeables: Closeable?) {
    closeables.forEach {
        it.safeClose(true)
    }
}
