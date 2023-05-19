package com.github.xs93.framework.core.ktx

import android.app.Application
import android.os.Build
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

/**
 *
 * Application扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 15:37
 * @email 466911254@qq.com
 */

/** 当前进程名称 */
val Application.currentProcessName: String
    get() {
        if (Build.VERSION.SDK_INT >= 28) {
            return Application.getProcessName()
        }
        return try {
            val cmdline = File("/proc/self/cmdline")
            FileInputStream(cmdline).buffered().use {
                val buffer = it.readBytes()
                    .dropLastWhile { b -> b.compareTo(0) == 0 }
                    .toByteArray()
                String(buffer, 0, buffer.size, Charset.forName("utf-8"))
            }
        } catch (throwable: Throwable) {
            packageName
        }
    }

/** 当前进程是否是主进程 */
val Application.isMainProcess: Boolean
    get() {
        return currentProcessName == packageName
    }