package com.github.xs93.wanandroid.app.ktx

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/3/19 16:09
 * @email 466911254@qq.com
 */

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}