package com.github.xs93.kv

import android.content.Context
import androidx.startup.Initializer
import com.tencent.mmkv.MMKV

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/23 14:06
 * @description AppStartup获取application
 *
 */
class KVInitializer : Initializer<Unit> {

    companion object {
        lateinit var appContext: Context
    }

    override fun create(context: Context) {
        appContext = context.applicationContext
        try {
            val clazz = Class.forName("com.tencent.mmkv.MMKV")
            if (clazz != null) {
                MMKV.initialize(context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}