package com.github.xs93.kv

import android.os.Parcelable
import java.io.Serializable

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/23 14:25
 * @description IKV 属性代理扩展
 *
 */

fun IKV.bool(key: String, defaultValue: Boolean = false) =
    KVProperty(key, defaultValue, { k, d -> getBool(k, d) }, { k, v -> putBool(k, v) })

fun IKV.int(key: String, defaultValue: Int = 0) =
    KVProperty(key, defaultValue, { k, d -> getInt(k, d) }, { k, v -> putInt(k, v) })

fun IKV.long(key: String, defaultValue: Long = 0L) =
    KVProperty(key, defaultValue, { k, d -> getLong(k, d) }, { k, v -> putLong(k, v) })

fun IKV.float(key: String, defaultValue: Float = 0.0f) =
    KVProperty(key, defaultValue, { k, d -> getFloat(k, d) }, { k, v -> putFloat(k, v) })

fun IKV.double(key: String, defaultValue: Double = 0.0) =
    KVProperty(key, defaultValue, { k, d -> getDouble(k, d) }, { k, v -> putDouble(k, v) })

fun IKV.string(key: String, defaultValue: String? = null) =
    KVProperty(key, defaultValue, { k, d -> getString(k, d) }, { k, v -> putString(k, v) })

fun IKV.stringSet(key: String, defaultValue: Set<String>? = null) =
    KVProperty(
        key,
        defaultValue,
        { k, d -> getStringSet(k, d) },
        { k, v -> putStringSet(k, v) })

fun IKV.bytes(key: String, defaultValue: ByteArray? = null) =
    KVProperty(key, defaultValue, { k, d -> getBytes(k, d) }, { k, v -> putBytes(k, v) })

inline fun <reified T : Serializable> IKV.serializable(key: String, defaultValue: T? = null) =
    KVProperty(
        key,
        defaultValue,
        { k, d -> getSerializable(k, T::class.java, d) ?: defaultValue },
        { k, v -> putSerializable(k, v) })

inline fun <reified T : Parcelable> IKV.parcelable(key: String, defaultValue: T? = null) =
    KVProperty(
        key,
        defaultValue,
        { k, d -> getParcelable(k, T::class.java, d) ?: defaultValue },
        { k, v -> putParcelable(k, v) })

fun <T> KVProperty<T>.asStateFlow() = KVStateFlowProperty(this)

