package com.github.xs93.framework.core.crypt

import android.util.Base64
import java.security.NoSuchAlgorithmException
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

    /**
     * 生成一个 AEC 秘钥
     * @param keySize Int  秘钥大小,取值只能是128位(16字节)，192位(24字节)，256位(32位字节)
     * @return ByteArray
     */
    fun generateKey(keySize: Int): ByteArray? {
        return try {
            val keyGenerate = KeyGenerator.getInstance("AES")
            keyGenerate.init(keySize)
            val secretKey = keyGenerate.generateKey()
            val keyBytes = secretKey.encoded
            keyBytes
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 生成一个AEC 向量数组
     * @param blockSize Int 向量长度
     * @return ByteArray 向量数组
     */
    fun generateIvBytes(blockSize: Int): ByteArray {
        val ivParams = ByteArray(blockSize)
        val random = java.util.Random()
        for (index in 0 until blockSize) {
            ivParams[index] = random.nextInt(256).toByte()
        }
        return ivParams
    }

    /**
     *  加密数据,加密后通过Base64编码返回
     * @param data String  需要加密的数据
     * @param password String 加密密码
     * @param iv String 加密向量
     * @return String? 加密后的数据,加密失败则返回null
     */
    fun encrypt(data: String, password: ByteArray, iv: ByteArray): String? {
        return try {
            val secretKeySpec = SecretKeySpec(password, "AES")
            val ivParameterSpec = IvParameterSpec(iv)
            //格式必须为"algorithm/mode/padding"或者"algorithm/",意为"算法/加密模式/填充方式"
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
            val resultByte = cipher.doFinal(data.toByteArray())
            Base64.encodeToString(resultByte, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 解密数据，加密数据需要通过Base64编码
     * @param data String  加密后的数据
     * @param password String 加密密码
     * @param iv String 加密向量
     * @return String? 解密后的原始数据,解析错误，返回null
     */
    fun decrypt(data: String, password: ByteArray, iv: ByteArray): String? {
        return try {
            val ivParameterSpec = IvParameterSpec(iv)
            val secretKeySpec = SecretKeySpec(password, "AES")

            //格式必须为"algorithm/mode/padding"或者"algorithm/",意为"算法/加密模式/填充方式"
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

            val encryptByteArray = Base64.decode(data, Base64.DEFAULT)
            val resultByte = cipher.doFinal(encryptByteArray)
            String(resultByte)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}