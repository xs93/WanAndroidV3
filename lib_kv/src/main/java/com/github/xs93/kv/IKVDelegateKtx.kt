package com.github.xs93.kv

import android.os.Parcelable
import androidx.annotation.NonNull
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/23 14:25
 * @description IKV 属性代理扩展
 *
 */

/** 基础数据类型代理方法 */
private inline fun <T> IKV.delegate(
    key: String,
    @NonNull defaultValue: T,
    crossinline getter: IKV.(String, T) -> T,
    crossinline setter: IKV.(String, T) -> Boolean
): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getter(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        setter(key, value)
    }
}

fun IKV.bool(key: String, defaultValue: Boolean = false): ReadWriteProperty<Any, Boolean> =
    delegate(key, defaultValue, IKV::getBool, IKV::putBool)

fun IKV.int(key: String, defaultValue: Int = 0): ReadWriteProperty<Any, Int> =
    delegate(key, defaultValue, IKV::getInt, IKV::putInt)

fun IKV.long(key: String, defaultValue: Long = 0L): ReadWriteProperty<Any, Long> =
    delegate(key, defaultValue, IKV::getLong, IKV::putLong)

fun IKV.float(key: String, defaultValue: Float = 0.0f): ReadWriteProperty<Any, Float> =
    delegate(key, defaultValue, IKV::getFloat, IKV::putFloat)

fun IKV.double(key: String, defaultValue: Double = 0.0): ReadWriteProperty<Any, Double> =
    delegate(key, defaultValue, IKV::getDouble, IKV::putDouble)

fun IKV.string(
    key: String,
    defaultValue: String? = null
): ReadWriteProperty<Any, String?> =
    delegate(key, defaultValue, IKV::getString, IKV::putString)

fun IKV.stringSet(
    key: String,
    defaultValue: Set<String>? = null
): ReadWriteProperty<Any, Set<String>?> =
    delegate(key, defaultValue, IKV::getStringSet, IKV::putStringSet)

fun IKV.bytes(
    key: String,
    defaultValue: ByteArray? = null
): ReadWriteProperty<Any, ByteArray?> =
    delegate(key, defaultValue, IKV::getBytes, IKV::putBytes)


inline fun <reified T : Serializable> IKV.delegateSerializable(
    key: String,
    defaultValue: T?
): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val decodeResult = getSerializable(key, T::class.java, defaultValue)
        return decodeResult ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        putSerializable(key, value)
    }
}

inline fun <reified T : Serializable> IKV.serializable(
    key: String,
    defaultValue: T? = null
): ReadWriteProperty<Any, T?> = delegateSerializable(key, defaultValue)

inline fun <reified T : Parcelable> IKVWithParcelable.delegateParcelable(
    key: String,
    defaultValue: T?
): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val decodeResult = getParcelable(key, T::class.java, defaultValue)
        return decodeResult ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        putParcelable(key, value)
    }
}

inline fun <reified T : Parcelable> IKVWithParcelable.parcelable(
    key: String,
    defaultValue: T? = null
): ReadWriteProperty<Any, T?> = delegateParcelable(key, defaultValue)


