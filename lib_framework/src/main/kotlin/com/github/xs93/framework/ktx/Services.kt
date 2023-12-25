package com.github.xs93.framework.ktx

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

/**
 *
 * Service相关扩展
 *
 * @author xushuai
 * @date   2022/9/2-12:06
 * @email  466911254@qq.com
 */

fun Context.startForegroundServiceCompat(intent: Intent) {
    ContextCompat.startForegroundService(this, intent)
}