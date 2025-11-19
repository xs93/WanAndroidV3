@file:Suppress("unused")

package com.github.xs93.utils

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 *
 * MD5加密
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/2/14 11:21
 * @email 466911254@qq.com
 */

fun String.hashWithMD5(): String {
    try {
        val md = MessageDigest.getInstance("MD5")
        val bytes = this.toByteArray(StandardCharsets.UTF_8)
        md.update(bytes)
        val digest = BigInteger(1, md.digest()).toString(16).padStart(32, '0')
        return digest
    } catch (e: Exception) {
        throw RuntimeException("MD5 hashing failed", e)
    }
}

fun String.hashWithSHA1(): String {
    try {
        val md = MessageDigest.getInstance("SHA-1")
        val bytes = this.toByteArray(StandardCharsets.UTF_8)
        md.update(bytes)
        val bigInt = BigInteger(1, md.digest())
        // 转换为16进制字符串，确保结果是固定的长度，不足的部分前面补0
        return String.format("%0" + (md.digestLength * 2) + "x", bigInt)
    } catch (e: Exception) {
        throw RuntimeException("SHA-1 hashing failed", e)
    }
}