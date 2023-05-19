package com.github.xs93.framework.network.cookie

import okhttp3.Cookie
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 *
 *
 *
 * @author xushuai
 * @date   2022/9/2-23:53
 * @email  466911254@qq.com
 */

fun String.toByteArray(): ByteArray {
    val result = uppercase()
    val byteArray = ByteArray(length / 2)
    val hexChar = result.toCharArray()
    for (i in indices step 2) {
        byteArray[i / 2] =
            ((charToByte(hexChar[i]).toInt() shl 4) or (charToByte(hexChar[i + 1]).toInt())).toByte()
    }
    return byteArray
}


private fun charToByte(c: Char): Byte {
    return "0123456789ABCDEF".indexOf(c).toByte()
}

fun ByteArray.toHexString(): String {
    val sb: StringBuilder = StringBuilder()
    for (element in this) {
        val v = element.toInt() and 0xff
        val hv = Integer.toHexString(v)
        if (hv.length < 2) {
            sb.append('0')
        }
        sb.append(hv)
    }
    return sb.toString()
}


fun String.toCookie(): Cookie? {
    val byteArray = toByteArray()
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
        baos.toByteArray().toHexString()
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