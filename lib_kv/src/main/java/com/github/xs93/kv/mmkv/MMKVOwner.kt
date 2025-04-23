package com.github.xs93.kv.mmkv

import android.os.Parcelable
import android.util.Base64
import com.github.xs93.kv.IKVWithParcelable
import com.tencent.mmkv.MMKV
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 *  使用MMKV 接口，获取一个可以自己初始化的mmkv对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/7/22 10:37
 * @email 466911254@qq.com
 */

open class MMKVOwner(
    override val mmapId: String,
    override val mode: Int = MMKV.SINGLE_PROCESS_MODE
) : IMMKVOwner {
    override val mmkv: MMKV by lazy { MMKV.mmkvWithID(mmapId, mode) }
}

interface IMMKVOwner : IKVWithParcelable {

    val mmapId: String

    val mode: Int

    val mmkv: MMKV

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

    override fun <T : Serializable> putSerializable(
        key: String,
        value: T?
    ): Boolean {
        if (value == null) {
            mmkv.putString(key, null)
            return true
        }
        val baos = ByteArrayOutputStream()
        var out: ObjectOutputStream? = null
        try {
            out = ObjectOutputStream(baos)
            out.writeObject(value)
            val encodeJson = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
            mmkv.putString(key, encodeJson)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                baos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                out?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Serializable> getSerializable(
        key: String,
        clazz: Class<T>,
        defaultValue: T?
    ): T? {
        val encodeStr = mmkv.getString(key, null)
        if (encodeStr == null) return defaultValue
        val decodeBuffer = Base64.decode(encodeStr, Base64.DEFAULT)

        val bais = ByteArrayInputStream(decodeBuffer)
        val ois: ObjectInputStream? = null
        try {
            val ois = ObjectInputStream(bais)
            val obj = ois.readObject() as T
            ois.close()
            return obj
        } catch (e: Exception) {
            e.printStackTrace()
            return defaultValue
        } finally {
            try {
                bais.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                ois?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun <T : Parcelable> putParcelable(key: String, value: T?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun <T : Parcelable> getParcelable(
        key: String,
        clazz: Class<T>,
        defaultValue: T?
    ): T? {
        return mmkv.decodeParcelable(key, clazz, defaultValue)
    }

    override fun containsKey(key: String): Boolean {
        return mmkv.containsKey(key)
    }

    override fun remove(key: String) {
        mmkv.removeValueForKey(key)
    }

    override fun clear() {
        mmkv.clearAll()
    }
}