package com.github.xs93.framework.core.utils

import java.math.BigInteger
import java.security.MessageDigest

/**
 *
 * MD5加密
 * @author XuShuai
 * @version v1.0
 * @date 2023/2/14 11:21
 * @email 466911254@qq.com
 */


fun String.md5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}


fun ByteArray.toHex(): String {
    return BigInteger(1, this).toString(16)
}

