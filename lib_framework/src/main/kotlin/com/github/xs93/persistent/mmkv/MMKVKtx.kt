package com.github.xs93.persistent.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * MMKV kotlin 扩展，使用属性代理的方式
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/2/11 13:52
 */

/** 基础数据类型代理方法 */
private inline fun <T> MMKV.delegate(
    key: String,
    defaultValue: T,
    crossinline getter: MMKV.(String, T) -> T,
    crossinline setter: MMKV.(String, T) -> Boolean
): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getter(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        setter(key, value)
    }
}

fun MMKV.boolean(key: String, defaultValue: Boolean = false): ReadWriteProperty<Any, Boolean> =
    delegate(key, defaultValue, MMKV::decodeBool, MMKV::encode)

fun MMKV.int(key: String, defaultValue: Int = 0): ReadWriteProperty<Any, Int> =
    delegate(key, defaultValue, MMKV::decodeInt, MMKV::encode)

fun MMKV.long(key: String, defaultValue: Long = 0L): ReadWriteProperty<Any, Long> =
    delegate(key, defaultValue, MMKV::decodeLong, MMKV::encode)

fun MMKV.float(key: String, defaultValue: Float = 0.0f): ReadWriteProperty<Any, Float> =
    delegate(key, defaultValue, MMKV::decodeFloat, MMKV::encode)

fun MMKV.double(key: String, defaultValue: Double = 0.0): ReadWriteProperty<Any, Double> =
    delegate(key, defaultValue, MMKV::decodeDouble, MMKV::encode)


/** 可以null默认值的代理方法 */
private inline fun <T> MMKV.nullableDefaultValueDelegate(
    key: String,
    defaultValue: T?,
    crossinline getter: MMKV.(String, T?) -> T,
    crossinline setter: MMKV.(String, T?) -> Boolean
): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return getter(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        setter(key, value)
    }
}

fun MMKV.byteArray(
    key: String,
    defaultValue: ByteArray? = null
): ReadWriteProperty<Any, ByteArray?> =
    nullableDefaultValueDelegate(key, defaultValue, MMKV::decodeBytes, MMKV::encode)

fun MMKV.string(key: String, defaultValue: String? = null): ReadWriteProperty<Any, String?> =
    nullableDefaultValueDelegate(key, defaultValue, MMKV::decodeString, MMKV::encode)

fun MMKV.stringSet(
    key: String,
    defaultValue: Set<String>? = null
): ReadWriteProperty<Any, Set<String>?> =
    nullableDefaultValueDelegate(key, defaultValue, MMKV::decodeStringSet, MMKV::encode)

/** 可以序列表对象代理方法,此方法可以保存为null的数据 */
inline fun <reified T : Parcelable> MMKV.parcelable(
    key: String,
    defaultValue: T? = null
): ReadWriteProperty<Any, T?> = object : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val decodeResult = decodeParcelable(key, T::class.java, defaultValue)
        return decodeResult ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        encode(key, value)
    }
}

/**保存数据，数据不能为null*/
inline fun <reified T : Parcelable> MMKV.parcelableWithNotNull(
    key: String? = null,
    defaultValue: T
): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val decodeResult = decodeParcelable(key ?: property.name, T::class.java, defaultValue)
        return decodeResult ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        encode(key ?: property.name, value)
    }
}

inline fun <reified T : Parcelable> MMKV.listParcelable(
    key: String,
    defaultValue: List<T>? = null
): ReadWriteProperty<Any, List<T>?> = object : ReadWriteProperty<Any, List<T>?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): List<T>? {
        if (!containsKey("$key-Size")) {
            return defaultValue
        }
        val result = mutableListOf<T>()
        val size = decodeInt("$key-Size", 0)
        if (size == 0) {
            return result
        }
        for (index in 0 until size) {
            val itemValue = decodeParcelable("$key-$index", T::class.java, null)
            itemValue?.let {
                result.add(itemValue)
            }
        }
        return result
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: List<T>?) {
        if (value.isNullOrEmpty()) {
            val oldSize = decodeInt("$key-Size", 0)
            for (index in 0 until oldSize) {
                if (decodeParcelable("$key-$index", T::class.java, null) != null) {
                    removeValueForKey("$key-$index")
                }
            }
            encode("$key-Size", 0)
        } else {
            for (index in value.indices) {
                encode("$key-$index", value[index])
            }
            encode("$key-Size", value.size)
        }
    }
}

inline fun <reified T : Parcelable> MMKV.listParcelableWithNotNull(
    key: String,
    defaultValue: List<T>
): ReadWriteProperty<Any, List<T>> = object : ReadWriteProperty<Any, List<T>> {
    override fun getValue(thisRef: Any, property: KProperty<*>): List<T> {
        if (!containsKey("$key-Size")) {
            return defaultValue
        }
        val result = mutableListOf<T>()
        val size = decodeInt("$key-Size", 0)
        if (size == 0) {
            return result
        }
        for (index in 0 until size) {
            val itemValue = decodeParcelable("$key-$index", T::class.java, null)
            itemValue?.let {
                result.add(itemValue)
            }
        }
        return result
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: List<T>) {
        if (value.isEmpty()) {
            val oldSize = decodeInt("$key-Size", 0)
            for (index in 0 until oldSize) {
                if (decodeParcelable("$key-$index", T::class.java, null) != null) {
                    removeValueForKey("$key-$index")
                }
            }
            encode("$key-Size", 0)
        } else {
            for (index in value.indices) {
                encode("$key-$index", value[index])
            }
            encode("$key-Size", value.size)
        }
    }
}