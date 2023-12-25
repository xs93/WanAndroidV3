package com.github.xs93.persistent.store

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 *  使用MMKV 接口，获取一个可以自己初始化的mmkv对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/7/22 10:37
 * @email 466911254@qq.com
 */
interface MMKVStore : IStore {

    val mmkv: MMKV get() = defaultMMKV

    companion object {

        private val defaultMMKV = MMKV.defaultMMKV()
    }

    override fun putBool(key: String, value: Boolean): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getBool(key: String, defaultValue: Boolean): Boolean {
        return mmkv.decodeBool(key, defaultValue)
    }

    override fun putInt(key: String, value: Int): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return mmkv.decodeInt(key, defaultValue)
    }

    override fun putLong(key: String, value: Long): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return mmkv.decodeLong(key, defaultValue)
    }

    override fun putFloat(key: String, value: Float): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return mmkv.decodeFloat(key, defaultValue)
    }

    override fun putDouble(key: String, value: Double): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return mmkv.decodeDouble(key, defaultValue)
    }

    override fun putString(key: String, value: String?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return mmkv.decodeString(key, defaultValue)
    }

    override fun putStringSet(key: String, value: Set<String>?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return mmkv.decodeStringSet(key, defaultValue)
    }

    override fun putBytes(key: String, value: ByteArray?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getBytes(key: String, defaultValue: ByteArray?): ByteArray? {
        return mmkv.decodeBytes(key, defaultValue)
    }

    override fun <T : Parcelable> putParcelable(key: String, value: T?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>, defaultValue: T?): T? {
        return mmkv.decodeParcelable(key, clazz, defaultValue)
    }
}