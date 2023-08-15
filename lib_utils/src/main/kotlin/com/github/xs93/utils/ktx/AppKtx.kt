package com.github.xs93.utils.ktx

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.*
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

/**
 * 应用相关信息扩展函数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 16:52
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

inline val Context.isDebug: Boolean
    get() = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0


inline val Context.appName: String
    get() {
        var appName = ""
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfoCompat(packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            appName = resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appName
    }

inline val Context.appVersionName: String
    get() {
        var versionName = ""
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfoCompat(packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

inline val Context.appVersionCode: Long
    get() {
        var versionCode: Long = -1L
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfoCompat(packageName, 0)
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }