package com.github.xs93.network.utils

import android.content.Context
import android.util.Base64
import com.github.xs93.utils.crypt.AESCrypt.decrypt
import com.github.xs93.utils.crypt.AESCrypt.encrypt
import com.github.xs93.utils.md5
import okhttp3.Request
import okio.Buffer
import java.io.File
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * 网络请求缓存工具类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/3 10:13
 * @email 466911254@qq.com
 */
object CacheUtils {


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
        return key.md5()
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
     * @param content 内容
     * @param flag    标志
     * @return 加密后内容
     */
    fun encryptContent(content: String, flag: String): String {
        try {
            val key = MessageDigest.getInstance("MD5").digest(flag.toByteArray(charset("UTF-8")))
            val encryptData = encrypt(content.toByteArray(), key)
            return Base64.encodeToString(encryptData, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    /**
     * 数据解密
     *
     * @param content 内容
     * @param flag    标志
     * @return 解密后内容
     */
    fun decryptContent(content: String, flag: String): String {
        try {
            val key = MessageDigest.getInstance("MD5").digest(flag.toByteArray(charset("UTF-8")))
            val decodeContent = Base64.decode(content, Base64.DEFAULT)
            val decodeData = decrypt(decodeContent, key)
            return String(decodeData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}