package com.github.xs93.wan.common.router

import android.content.Intent
import androidx.core.net.toUri
import com.github.xs93.utils.AppInject

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/7 16:14
 * @description 跳转路由
 *
 */
object Router {

    /**
     * 跳转登录界面
     */
    fun toLogin() {
        val url = RouterIntentUri.LOGIN.toUri()
        val intent = Intent(RouterIntentAction.LOGIN, url).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        AppInject.getApp().startActivity(intent)
    }
}