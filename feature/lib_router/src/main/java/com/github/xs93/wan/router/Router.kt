package com.github.xs93.wan.router

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.core.net.toUri
import com.github.xs93.core.AppInject

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
        try {
            val url = RouterIntentUri.LOGIN.toUri()
            val intent = Intent(Intent.ACTION_VIEW, url).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            AppInject.getApp().startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}