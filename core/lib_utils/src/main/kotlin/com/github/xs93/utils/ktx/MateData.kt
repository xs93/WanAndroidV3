package com.github.xs93.utils.ktx

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle

/**
 * MateData 数据扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 16:31
 * @email 466911254@qq.com
 */


fun Context.getAppMateData(): Bundle {
    val info = packageManager.getApplicationInfoCompat(packageName, PackageManager.GET_META_DATA)
    return info.metaData
}

fun Context.getActivityMateData(clazz: String): Bundle {
    val componentName = ComponentName(this, clazz)
    val info = packageManager.getActivityInfoCompat(componentName, PackageManager.GET_META_DATA)
    return info.metaData
}

fun Context.getActivityMateData(clazz: Class<*>): Bundle {
    val componentName = ComponentName(this, clazz)
    val info = packageManager.getActivityInfoCompat(componentName, PackageManager.GET_META_DATA)
    return info.metaData
}

fun Context.getServiceMateData(clazz: String): Bundle {
    val componentName = ComponentName(this, clazz)
    val info = packageManager.getServiceInfoCompat(componentName, PackageManager.GET_META_DATA)
    return info.metaData
}

fun Context.getServiceMateData(clazz: Class<*>): Bundle {
    val componentName = ComponentName(this, clazz)
    val info = packageManager.getServiceInfoCompat(componentName, PackageManager.GET_META_DATA)
    return info.metaData
}

fun Context.getReceiverMateData(clazz: String): Bundle {
    val componentName = ComponentName(this, clazz)
    val info = packageManager.getReceiverInfoCompat(componentName, PackageManager.GET_META_DATA)
    return info.metaData
}

fun Context.getReceiverMateData(clazz: Class<*>): Bundle {
    val componentName = ComponentName(this, clazz)
    val info = packageManager.getReceiverInfoCompat(componentName, PackageManager.GET_META_DATA)
    return info.metaData
}

fun Context.getProviderMateData(clazz: String): Bundle {
    val componentName = ComponentName(this, clazz)
    val info = packageManager.getProviderInfoCompat(componentName, PackageManager.GET_META_DATA)
    return info.metaData
}

fun Context.getProviderMateData(clazz: Class<*>): Bundle {
    val componentName = ComponentName(this, clazz)
    val info = packageManager.getProviderInfoCompat(componentName, PackageManager.GET_META_DATA)
    return info.metaData
}