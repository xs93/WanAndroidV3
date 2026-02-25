package com.github.xs93.network.utils

import android.content.Context
import android.util.Base64
import okhttp3.Request
import okio.Buffer
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 网络请求缓存工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/3 10:13
 * @email 466911254@qq.com
 */
object CacheUtils {

    private const val TRANSFORMATION = "AES/CBC/PKCS7PADDING"

    /**
     * 缓存文件夹
     * @param context Context
     * @return File
     */
    fun getCacheDir(context: Context): File {
        val cachePath = context.externalCacheDir?.absolutePath + "/httpCache"
        val cacheFile = File(cachePath)
        if (!cacheFile.exists()) {
            cacheFile.mkdirs()
        }
        return cacheFile
    }


    /**
     * 获取接口数据缓存key
     *
     * @param request Request
     * @return 接口数据缓存key
     */
    fun getCacheKey(request: Request): String {
        val key = "${getRequestUrl(request)}?${getPostParams(request)}"
        return runCatching {
            getHashMD5(key)
        }.getOrDefault(key)
    }

    /**
     * 获取POST请求Url
     *
     * @param request Request
     * @return 请求Url
     */
    fun getRequestUrl(request: Request): String {
        return request.url.toString()
    }

    /**
     * 获取POST请求参数
     *
     * @param request Request
     * @return 请求参数
     */
    private fun getPostParams(request: Request): String {
        val method: String = request.method
        if ("POST" == method) {
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            return buffer.readString(Charset.defaultCharset())
        }
        return ""
    }

    /**
     * MD5 加密数据内容
     *
     * @param key     密钥
     * @param iv      偏移量
     * @param content 内容
     * @return 加密后内容
     */
    fun encryptContent(key: String, iv: String, content: String): String {
        return runCatching {
            val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
            val ivParameterSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)
            val encryptBytes = cipher.doFinal(content.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(encryptBytes, Base64.NO_WRAP)
        }.getOrDefault("")
    }


    /**
     * 数据解密
     *  @param key    密钥
     * @param iv      偏移量
     * @param content Base64 编译后内容
     * @return 解密后内容
     */
    fun decryptContent(key: String, iv: String, content: String): String {
        return runCatching {
            val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
            val ivParameterSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)
            val decodeString = Base64.decode(content, Base64.NO_WRAP)
            String(cipher.doFinal(decodeString))
        }.getOrDefault("")
    }


    @Throws(RuntimeException::class)
    private fun getHashMD5(content: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            val bytes = content.toByteArray(StandardCharsets.UTF_8)
            md.update(bytes)
            val digest = BigInteger(1, md.digest()).toString(16).padStart(32, '0')
            return digest
        } catch (e: Exception) {
            throw RuntimeException("MD5 hashing failed", e)
        }
    }
}