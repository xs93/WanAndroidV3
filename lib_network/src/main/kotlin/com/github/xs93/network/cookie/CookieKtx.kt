@file:OptIn(ExperimentalStdlibApi::class)

package com.github.xs93.network.cookie

import okhttp3.Cookie
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 *
 * Cookie 相关扩展
 *
 * @author xushuai
 * @date   2022/9/2-23:53
 * @email  466911254@qq.com
 */

fun String.toCookie(): Cookie? {
    val byteArray = hexToByteArray(HexFormat.Default)
    val byteArrayInputStream = ByteArrayInputStream(byteArray)
    val cookie = try {
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        (objectInputStream.readObject() as SerializableCookie).getCookie()
    } catch (e: Exception) {
        null
    }
    return cookie
}

fun Cookie.toHexString(): String? {
    val serializableCookie = SerializableCookie(this)
    val baos = ByteArrayOutputStream()
    var oos: ObjectOutputStream? = null
    val result = try {
        oos = ObjectOutputStream(baos)
        oos.writeObject(serializableCookie)
        baos.toByteArray().toHexString(HexFormat.Default)
    } catch (e: Exception) {
        null
    } finally {
        try {
            oos?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}


fun Cookie.isExpired(): Boolean {
    return expiresAt < System.currentTimeMillis()
}

val Cookie.token: String
    get() {
        return "$name@$domain"
    }