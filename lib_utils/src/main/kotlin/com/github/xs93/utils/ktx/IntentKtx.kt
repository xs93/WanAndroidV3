@file:Suppress("unused")

package com.github.xs93.utils.ktx

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 *
 * 序列化传输扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/2/28 17:03
 * @email 466911254@qq.com
 */

fun <T : Parcelable> Bundle.getParcelableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(key)
    }
}

fun <T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        @Suppress("UNCHECKED_CAST", "DEPRECATION")
        getSerializable(key) as T?
    }
}

fun <T : Parcelable> Bundle.getParcelableArrayCompat(key: String, clazz: Class<T>): Array<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArray(key, clazz)
    } else {
        @Suppress("UNCHECKED_CAST", "DEPRECATION")
        getParcelableArray(key) as Array<T>?
    }
}

fun <T : Parcelable> Bundle.getParcelableArrayListCompat(
    key: String,
    clazz: Class<T>
): ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayList(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getParcelableArrayList(key)
    }
}


fun <T : Parcelable> Intent.getParcelableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(key)
    }
}

fun <T : Serializable> Intent.getSerializableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, clazz)
    } else {
        @Suppress("UNCHECKED_CAST", "DEPRECATION")
        getSerializableExtra(key) as T?
    }
}

fun <T : Parcelable> Intent.getParcelableArrayCompat(key: String, clazz: Class<T>): Array<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayExtra(key, clazz)
    } else {
        @Suppress("UNCHECKED_CAST", "DEPRECATION")
        getParcelableArrayExtra(key) as Array<T>?
    }
}

fun <T : Parcelable> Intent.getParcelableArrayListCompat(
    key: String,
    clazz: Class<T>
): ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayListExtra(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getParcelableArrayListExtra(key)
    }
}