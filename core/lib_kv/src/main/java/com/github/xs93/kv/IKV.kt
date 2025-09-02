package com.github.xs93.kv

import android.os.Parcelable
import java.io.Serializable

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/7/23
 * @description key-value 存储接口
 *
 */
interface IKV {

    fun putBool(key: String, value: Boolean, isSync: Boolean = true): Boolean
    fun getBool(key: String, defaultValue: Boolean = false): Boolean

    fun putInt(key: String, value: Int, isSync: Boolean = true): Boolean
    fun getInt(key: String, defaultValue: Int = 0): Int

    fun putLong(key: String, value: Long, isSync: Boolean = true): Boolean
    fun getLong(key: String, defaultValue: Long = 0L): Long

    fun putFloat(key: String, value: Float, isSync: Boolean = true): Boolean
    fun getFloat(key: String, defaultValue: Float = 0.0f): Float

    fun putDouble(key: String, value: Double, isSync: Boolean = true): Boolean
    fun getDouble(key: String, defaultValue: Double = 0.0): Double

    fun putString(key: String, value: String?, isSync: Boolean = true): Boolean
    fun getString(key: String, defaultValue: String? = null): String?

    fun putStringSet(key: String, value: Set<String>?, isSync: Boolean = true): Boolean
    fun getStringSet(key: String, defaultValue: Set<String>? = null): Set<String>?

    fun putBytes(key: String, value: ByteArray?, isSync: Boolean = true): Boolean
    fun getBytes(key: String, defaultValue: ByteArray? = null): ByteArray?

    fun <T : Serializable> putSerializable(key: String, value: T?, isSync: Boolean = true): Boolean
    fun <T : Serializable> getSerializable(
        key: String,
        clazz: Class<T>,
        defaultValue: T? = null
    ): T?

    fun <T : Parcelable> putParcelable(key: String, value: T?, isSync: Boolean = true): Boolean
    fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>, defaultValue: T? = null): T?

    fun containsKey(key: String): Boolean

    fun remove(key: String)

    fun clear()
}