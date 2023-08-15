package com.github.xs93.persistent.mmkv

import android.os.Parcelable
import com.github.xs93.persistent.store.MMKVStore

/**
 *
 * MMKVOwner 扩展
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 12:56
 * @email 466911254@qq.com
 */

fun MMKVStore.mmkvBoolean(key: String, defaultValue: Boolean = false) = mmkv.boolean(key, defaultValue)

fun MMKVStore.mmkvInt(key: String, defaultValue: Int = 0) = mmkv.int(key, defaultValue)

fun MMKVStore.mmkvLong(key: String, defaultValue: Long = 0L) = mmkv.long(key, defaultValue)

fun MMKVStore.mmkvFloat(key: String, defaultValue: Float = 0.0f) = mmkv.float(key, defaultValue)

fun MMKVStore.mmkvDouble(key: String, defaultValue: Double = 0.0) = mmkv.double(key, defaultValue)

fun MMKVStore.mmkvByteArray(key: String, defaultValue: ByteArray? = null) = mmkv.byteArray(key, defaultValue)

fun MMKVStore.mmkvString(key: String, defaultValue: String? = null) = mmkv.string(key, defaultValue)

fun MMKVStore.mmkvStringSet(key: String, defaultValue: Set<String>? = null) = mmkv.stringSet(key, defaultValue)

inline fun <reified T : Parcelable> MMKVStore.mmkvParcelable(key: String, defaultValue: T) =
    mmkv.parcelableWithNotNull(key, defaultValue)

inline fun <reified T : Parcelable> MMKVStore.mmkvParcelableWithNull(key: String, defaultValue: T? = null) =
    mmkv.parcelable(key, defaultValue)

inline fun <reified T : Parcelable> MMKVStore.mmkvListParcelable(key: String, defaultValue: List<T>) =
    mmkv.listParcelableWithNotNull(key, defaultValue)

inline fun <reified T : Parcelable> MMKVStore.mmkvListParcelableWithNull(key: String, defaultValue: List<T>? = null) =
    mmkv.listParcelable(key, defaultValue)
