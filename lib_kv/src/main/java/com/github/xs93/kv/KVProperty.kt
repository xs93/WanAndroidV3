package com.github.xs93.kv

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 9:06
 * @description IKV 属性代理
 *
 */
open class KVProperty<T>(
    val key: String,
    val defaultValue: T,
    internal val decode: (String, T) -> T,
    internal val encode: (String, T) -> Boolean
) : ReadWriteProperty<IKV, T> {
    override fun getValue(
        thisRef: IKV,
        property: KProperty<*>
    ): T = decode(key, defaultValue)


    override fun setValue(thisRef: IKV, property: KProperty<*>, value: T) {
        encode(key, value)
    }
}