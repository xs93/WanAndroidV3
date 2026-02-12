package com.github.xs93.core.ktx

import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ProviderInfo
import android.content.pm.ServiceInfo
import android.os.Build

/**
 *
 * PackageManager相关扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/1 13:35
 * @email 466911254@qq.com
 */
fun PackageManager.getApplicationInfoCompat(packageName: String, flags: Int): ApplicationInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(flags.toLong()))
    } else {
        getApplicationInfo(packageName, flags)
    }
}

fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int): PackageInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        getPackageInfo(packageName, flags)
    }
}

fun PackageManager.getActivityInfoCompat(component: ComponentName, flags: Int): ActivityInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getActivityInfo(component, PackageManager.ComponentInfoFlags.of(flags.toLong()))
    } else {
        getActivityInfo(component, flags)
    }
}

fun PackageManager.getServiceInfoCompat(component: ComponentName, flags: Int): ServiceInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getServiceInfo(component, PackageManager.ComponentInfoFlags.of(flags.toLong()))
    } else {
        getServiceInfo(component, flags)
    }
}

fun PackageManager.getReceiverInfoCompat(component: ComponentName, flags: Int): ActivityInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getReceiverInfo(component, PackageManager.ComponentInfoFlags.of(flags.toLong()))
    } else {
        getReceiverInfo(component, flags)
    }
}

fun PackageManager.getProviderInfoCompat(component: ComponentName, flags: Int): ProviderInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getProviderInfo(component, PackageManager.ComponentInfoFlags.of(flags.toLong()))
    } else {
        getProviderInfo(component, flags)
    }
}