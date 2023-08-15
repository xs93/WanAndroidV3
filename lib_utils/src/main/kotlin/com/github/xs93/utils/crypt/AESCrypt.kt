package com.github.xs93.utils.crypt

import android.util.Base64
import com.github.xs93.utils.toHexString
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES 加解密
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/4/13 10:48
 * @email 466911254@qq.com
 */
object AESCrypt {

    private const val transformation = "AES/CBC/PKCS5Padding"

    /**
     * 生成一个 AEC 秘钥
     * @param keySize Int  秘钥大小,取值只能是128位(16字节)，192位(24字节)，256位(32位字节)
     * @return ByteArray
     */
    fun generateKey(keySize: Int): String {
        val keyGenerate = KeyGenerator.getInstance("AES")
        keyGenerate.init(keySize)
        val secretKey = keyGenerate.generateKey()
        val keyBytes = secretKey.encoded
        return keyBytes.toHexString()
    }


    /**
     *  加密数据,加密后通过Base64编码返回
     * @param data String  需要加密的数据
     * @param password String 加密密码
     * @return String? 加密后的数据,加密失败则返回null
     */
    fun encrypt(data: String, password: String): String {
        val resultByte = encrypt(data.toByteArray(), password.toByteArray())
        return Base64.encodeToString(resultByte, Base64.DEFAULT)
    }


    /**
     *  加密数据,加密后通过Base64编码返回
     * @param data ByteArray  需要加密的数据byte数组
     * @param password ByteArray 加密密码 byte数组
     * @return ByteArray? 加密后的数据
     */
    fun encrypt(data: ByteArray, password: ByteArray): ByteArray {
        val keySpec = SecretKeySpec(password, "AES")
        val ivParameterSpec = IvParameterSpec(password)

        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)
        return cipher.doFinal(data)
    }

    /**
     *  AES 解密数据
     * @param data String 加密数据
     * @param password String 解密秘钥
     * @return String 原数据
     */

    fun decrypt(data: String, password: String): String {
        val resultByte = decrypt(data.toByteArray(), password.toByteArray())
        return String(resultByte)
    }


    /**
     *  AES 解密数据
     * @param data String 加密数据
     * @param password String 解密秘钥
     * @return String 原数据
     */

    fun decrypt(data: ByteArray, password: ByteArray): ByteArray {
        val keySpec = SecretKeySpec(password, "AES")
        val ivParameterSpec = IvParameterSpec(password)
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)
        return cipher.doFinal(data)
    }
}