package com.github.xs93.framework.core.ktx

import android.content.Context
import android.content.Intent
import android.os.Build

/**
 *
 * Service相关扩展
 *
 * @author xushuai
 * @date   2022/9/2-12:06
 * @email  466911254@qq.com
 */

fun Context.startForegroundServiceCompat(intent: Intent) {
    if (Build.VERSION.SDK_INT >= 26) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}