package com.github.xs93.network.cookie

import okhttp3.Cookie
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 *
 *  可序列化的Cookie包装对象
 *
 * @author xushuai
 * @date   2022/9/2-17:18
 * @email  466911254@qq.com
 */
class SerializableCookie(
    @field:Transient
    private val cookie: Cookie,
) : Serializable {

    companion object {

        private const val serialVersionUID = -86L

        private const val NOT_VALUE_EXPIRES_AT = -1L
    }

    @Transient
    private var mClientCookie: Cookie? = null


    fun getCookie(): Cookie {
        return mClientCookie ?: cookie
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(ois: ObjectInputStream) {
        val name: String = ois.readObject() as String
        val value: String = ois.readObject() as String
        val expiresAt: Long = ois.readLong()
        val domain: String = ois.readObject() as String
        val path: String = ois.readObject() as String
        val secure = ois.readBoolean()
        val httpOnly = ois.readBoolean()
        val hostOnly = ois.readBoolean()

        val cookie = Cookie.Builder().apply {
            name(name)
            value(value)
            if (expiresAt != NOT_VALUE_EXPIRES_AT) {
                expiresAt(expiresAt)
            }
            path(path)
            if (hostOnly) hostOnlyDomain(domain) else domain(domain)
            if (secure) {
                secure()
            }
            if (httpOnly) {
                httpOnly()
            }
        }.build()
        mClientCookie = cookie
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(cookie.name)
        out.writeObject(cookie.value)
        out.writeLong(if (cookie.persistent) cookie.expiresAt else NOT_VALUE_EXPIRES_AT)
        out.writeObject(cookie.domain)
        out.writeObject(cookie.path)
        out.writeBoolean(cookie.secure)
        out.writeBoolean(cookie.httpOnly)
        out.writeBoolean(cookie.hostOnly)
    }
}