package com.github.xs93.utils.ktx

import android.content.Context
import androidx.core.content.ContextCompat

/**
 * Android 权限相关扩展方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/15 10:56
 * @email 466911254@qq.com
 */


fun Context.checkSelfPermissionCompat(permission: String): Int {
    return ContextCompat.checkSelfPermission(this, permission)
}
