package com.github.xs93.framework.network.utils

import android.content.Context
import com.github.xs93.framework.core.utils.md5
import okhttp3.Request
import okio.Buffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 *
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
     * 加密
     *
     * @param content 内容
     * @param flag    标志
     * @return 加密后内容
     */
    fun encryptContent(content: String, flag: String): String {
        try {
            val key = MessageDigest.getInstance("MD5").digest(flag.toByteArray(charset("UTF-8")))
            val encryptData = encrypt(content.toByteArray(), key)
            return byteArrayToHexString(encryptData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return content
    }


    /**
     * 解密
     *
     * @param content 内容
     * @param flag    标志
     * @return 解密后内容
     */
    fun decryptContent(content: String, flag: String): String {
        var finalContent = content
        try {
            val key = MessageDigest.getInstance("MD5").digest(flag.toByteArray(charset("UTF-8")))
            var dataLength = 0
            if (finalContent.contains("|")) {
                val contentArray = finalContent.split("\\|".toRegex()).toTypedArray()
                if (contentArray.size == 2) {
                    finalContent = contentArray[0]
                    dataLength = contentArray[1].toInt()
                }
            }
            val encryptData: ByteArray = hexStr2Byte(finalContent)
            val decryptData = decrypt(encryptData, key, dataLength)
            return String(decryptData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return finalContent
    }

    /**
     * 十六进制字符串转byte[]
     *
     * @param hex 十六进制字符串
     * @return byte[]
     */
    private fun hexStr2Byte(hex: String): ByteArray {
        var finalHex = hex
        // 奇数位补0
        if (finalHex.length % 2 != 0) {
            finalHex = "0$finalHex"
        }
        val length = finalHex.length
        val buffer = ByteBuffer.allocate(length / 2)
        var i = 0
        while (i < length) {
            var hexStr = finalHex[i].toString()
            i++
            hexStr += finalHex[i]
            val b = hexStr.toInt(16).toByte()
            buffer.put(b)
            i++
        }
        return buffer.array()
    }

    /**
     * byte[]转十六进制字符串
     *
     * @param array byte[]
     * @return 十六进制字符串
     */
    private fun byteArrayToHexString(array: ByteArray): String {
        val buffer = StringBuffer()
        for (i in array.indices) {
            buffer.append(byteToHex(array[i]))
        }
        return buffer.toString()
    }

    /**
     * byte转十六进制字符
     *
     * @param b byte
     * @return 十六进制字符
     */
    private fun byteToHex(b: Byte): String {
        var hex = Integer.toHexString(b.toInt() and 0xFF)
        if (hex.length == 1) {
            hex = "0$hex"
        }
        return hex.uppercase(Locale.getDefault())
    }


    /**
     * AES加密
     *
     * @param data 将要加密的内容
     * @param key  密钥
     * @return 已经加密的内容
     */
    fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        var finalData = data
        // 不足16字节，补齐内容为0
        val len = 16 - finalData.size % 16
        for (i in 0 until len) {
            val bytes = byteArrayOf(0.toByte())
            finalData = concatArray(finalData, bytes)
        }
        try {
            val secretKeySpec = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/ECB/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            return cipher.doFinal(finalData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }

    /**
     * AES解密
     *
     * @param data   将要解密的内容
     * @param key    密钥
     * @param length 数据长度
     * @return 已经解密的内容
     */
    fun decrypt(data: ByteArray, key: ByteArray, length: Int): ByteArray {
        try {
            val secretKeySpec = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/ECB/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            return noPadding(cipher.doFinal(data), length)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }

    /**
     * 合并数组
     *
     * @param firstArray  第一个数组
     * @param secondArray 第二个数组
     * @return 合并后的数组
     */
    private fun concatArray(firstArray: ByteArray, secondArray: ByteArray): ByteArray {
        val bytes = ByteArray(firstArray.size + secondArray.size)
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.size)
        System.arraycopy(secondArray, 0, bytes, firstArray.size, secondArray.size)
        return bytes
    }

    /**
     * 去除数组中的补齐
     *
     * @param paddingBytes 源数组
     * @param dataLength   去除补齐后的数据长度
     * @return 去除补齐后的数组
     */
    private fun noPadding(paddingBytes: ByteArray, dataLength: Int): ByteArray {
        val noPaddingBytes: ByteArray
        if (dataLength > 0) {
            if (paddingBytes.size > dataLength) {
                noPaddingBytes = ByteArray(dataLength)
                System.arraycopy(paddingBytes, 0, noPaddingBytes, 0, dataLength)
            } else {
                noPaddingBytes = paddingBytes
            }
        } else {
            val index = paddingIndex(paddingBytes)
            if (index > 0) {
                noPaddingBytes = ByteArray(index)
                System.arraycopy(paddingBytes, 0, noPaddingBytes, 0, index)
            } else {
                noPaddingBytes = paddingBytes
            }
        }
        return noPaddingBytes
    }

    /**
     * 获取补齐的位置
     *
     * @param paddingBytes 源数组
     * @return 补齐的位置
     */
    private fun paddingIndex(paddingBytes: ByteArray): Int {
        for (i in paddingBytes.indices.reversed()) {
            if (paddingBytes[i].toInt() != 0) {
                return i + 1
            }
        }
        return -1
    }
}