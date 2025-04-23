package com.github.xs93.kv

import android.os.Parcelable

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/23 10:03
 * @description 带有Parcelable接口的Store
 *
 */
interface IKVWithParcelable : IKV {
    fun <T : Parcelable> putParcelable(key: String, value: T?): Boolean
    fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>, defaultValue: T? = null): T?
}