@file:Suppress("unused")

package com.github.xs93.utils.ktx

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.IntentCompat
import androidx.core.os.BundleCompat
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
    return BundleCompat.getParcelable(this, key, clazz)
}

@Suppress("UNCHECKED_CAST", "DEPRECATION")
fun <T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        getSerializable(key) as T?
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Parcelable> Bundle.getParcelableArrayCompat(key: String, clazz: Class<out T>): Array<T>? {
    return BundleCompat.getParcelableArray(this, key, clazz) as Array<T>?
}

fun <T : Parcelable> Bundle.getParcelableArrayListCompat(
    key: String,
    clazz: Class<T>
): ArrayList<T>? {
    return BundleCompat.getParcelableArrayList(this, key, clazz)
}


fun <T : Parcelable> Intent.getParcelableCompat(key: String, clazz: Class<T>): T? {
    return IntentCompat.getParcelableExtra(this, key, clazz)
}

@Suppress("UNCHECKED_CAST", "DEPRECATION")
fun <T : Serializable> Intent.getSerializableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, clazz)
    } else {
        getSerializableExtra(key) as T?
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Parcelable> Intent.getParcelableArrayCompat(key: String, clazz: Class<T>): Array<T>? {
    return IntentCompat.getParcelableArrayExtra(this, key, clazz) as Array<T>?
}

fun <T : Parcelable> Intent.getParcelableArrayListCompat(
    key: String,
    clazz: Class<T>
): ArrayList<T>? {
    return IntentCompat.getParcelableArrayListExtra(this, key, clazz)
}