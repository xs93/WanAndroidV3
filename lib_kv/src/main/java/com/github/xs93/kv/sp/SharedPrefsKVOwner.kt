package com.github.xs93.kv.sp

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import com.github.xs93.kv.IKV
import com.github.xs93.kv.KVInitializer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/22 9:26
 * @description SharedPreferences 实现IKV接口
 *
 */

open class SharedPrefsKVOwner(override val name: String) : ISharedPrefsKVOwner {
    override val sharedPreferences: SharedPreferences by lazy {
        val context = KVInitializer.appContext
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}

interface ISharedPrefsKVOwner : IKV {

    val name: String
    val sharedPreferences: SharedPreferences
    val userCommit: Boolean get() = true

    override fun putBool(key: String, value: Boolean): Boolean {
        edit { putBoolean(key, value) }
        return true
    }

    override fun getBool(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun putInt(key: String, value: Int): Boolean {
        edit { putInt(key, value) }
        return true
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override fun putLong(key: String, value: Long): Boolean {
        edit { putLong(key, value) }
        return true
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    override fun putFloat(key: String, value: Float): Boolean {
        edit { putFloat(key, value) }
        return true
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    override fun putDouble(key: String, value: Double): Boolean {
        edit { putFloat(key, value.toFloat()) }
        return true
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return sharedPreferences.getFloat(key, defaultValue.toFloat()).toDouble()
    }

    override fun putString(key: String, value: String?): Boolean {
        edit { putString(key, value) }
        return true
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    override fun putStringSet(
        key: String,
        value: Set<String>?
    ): Boolean {
        edit { putStringSet(key, value) }
        return true
    }

    override fun getStringSet(
        key: String,
        defaultValue: Set<String>?
    ): Set<String>? {
        return sharedPreferences.getStringSet(key, defaultValue)
    }

    override fun putBytes(key: String, value: ByteArray?): Boolean {
        if (value == null) {
            edit { putString(key, null) }
        } else {
            val encodedString = Base64.encodeToString(value, Base64.DEFAULT)
            edit { putString(key, encodedString) }
        }
        return true
    }

    override fun getBytes(key: String, defaultValue: ByteArray?): ByteArray? {
        val encodedString = sharedPreferences.getString(key, null)
        if (encodedString == null) return null
        return Base64.decode(encodedString, Base64.DEFAULT)
    }


    override fun <T : Serializable> putSerializable(
        key: String,
        value: T?
    ): Boolean {
        if (value == null) {
            edit { putString(key, null) }
            return true
        }
        val baos = ByteArrayOutputStream()
        var out: ObjectOutputStream? = null
        try {
            out = ObjectOutputStream(baos)
            out.writeObject(value)
            val encodeJson = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
            edit { putString(key, encodeJson) }
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
        val encodeStr = sharedPreferences.getString(key, null)
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

    override fun containsKey(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    override fun remove(key: String) {
        edit { remove(key) }
    }

    override fun clear() {
        edit { clear() }
    }

    private fun edit(action: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences.edit(userCommit) {
            action()
        }
    }
}