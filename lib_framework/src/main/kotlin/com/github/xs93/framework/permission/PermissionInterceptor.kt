package com.github.xs93.framework.permission

import android.content.Context

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/9 13:23
 * @description 权限申请拦截器接口，用于拦截权限申请，目的在权限申请前做一些处理
 *
 */
interface PermissionInterceptor {

    fun shouldIntercept(context: Context, permissions: List<String>): Boolean

    fun intercept(context: Context, permissions: List<String>, callback: () -> Unit)
}