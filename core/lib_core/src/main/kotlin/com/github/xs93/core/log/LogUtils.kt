package com.github.xs93.core.log

import android.util.Log
import com.github.xs93.core.AppInject
import com.github.xs93.core.ktx.isDebug

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/9/15 15:39
 * @description
 *
 */


fun logD(tag: String, msg: String) {
    if (AppInject.getApp().isDebug) {
        Log.d(tag, msg)
    }
}

fun logE(tag: String, msg: String, e: Throwable? = null) {
    if (AppInject.getApp().isDebug) {
        Log.e(tag, msg, e)
    }
}