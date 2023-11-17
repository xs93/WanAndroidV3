@file:Suppress("unused")

package com.github.xs93.utils.crypt

import android.util.Base64
import com.github.xs93.utils.hexStringToByteArray
import com.github.xs93.utils.toHexString
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
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

    private const val TRANSFORMATION = "AES/CBC/PKCS7PADDING"
    private const val CHARSET = "UTF-8"
    private const val CIPHER = "AES"
    private const val HASH_ALGORITHM = "SHA-256"
    private val IV_BYTES =
        byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)

    // region 秘钥相关处理
    /**
     * 生成一个 AEC 秘钥
     * @param keySize Int  秘钥大小,取值只能是128位(16字节)，192位(24字节)，256位(32位字节)
     * @return ByteArray
     */
    fun generateKey(keySize: Int): String {
        val keyGenerate = KeyGenerator.getInstance(CIPHER)
        keyGenerate.init(keySize)
        val secretKey = keyGenerate.generateKey()
        val keyBytes = secretKey.encoded
        return bytesToString(keyBytes)
    }

    /**
     * 随机生成一个IV偏移量,加密解密需要同一个偏移量
     * @param transformation String 加密解密模式
     * @return String
     */
    fun generateIv(transformation: String = TRANSFORMATION): String {
        val cipher = Cipher.getInstance(transformation)
        val secureRandom = SecureRandom()
        val ivByte = ByteArray(cipher.blockSize)
        secureRandom.nextBytes(ivByte)
        return bytesToString(ivByte)
    }

    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA256 of the password
     */
    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val bytes = password.toByteArray(charset(CHARSET))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, CIPHER)
    }
    // endregion

    /**
     * 加密数据,加密后通过Base64编码返回
     * @param key ByteArray  密码 byte数组
     * @param iv ByteArray 偏移量数组
     * @param transformation String -the name of the transformation, e.g., DES/CBC/PKCS5Padding. See the Cipher
     * section in the Java Cryptography Architecture Standard Algorithm Name Documentation for information about standard transformation names.
     * @param message ByteArray  需要加密的数据byte数组
     *
     * @return ByteArray? 加密后的数据
     */
    fun encrypt(
        key: ByteArray,
        iv: ByteArray,
        transformation: String = TRANSFORMATION,
        message: ByteArray,
    ): ByteArray {
        val keySpec = SecretKeySpec(key, "AES")
        val ivParameterSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)
        return cipher.doFinal(message)
    }


    /**
     * 加密数据,加密后通过Base64编码返回
     * @param key 加密密码
     * @param iv 偏移量字符串
     * @param transformation String -the name of the transformation, e.g., DES/CBC/PKCS5Padding. See the Cipher
     * section in the Java Cryptography Architecture Standard Algorithm Name Documentation for information about standard transformation names.
     * @param message String  需要加密的数据
     * @return String? 加密后的数据,加密失败则返回null
     */
    fun encrypt(key: String, iv: String, transformation: String = TRANSFORMATION, message: String): String {
        val resultByte = encrypt(
            stringToBytes(key),
            stringToBytes(iv),
            transformation,
            message.toByteArray(charset(CHARSET)),
        )
        return Base64.encodeToString(resultByte, Base64.NO_WRAP)
    }

    /**
     * AES 解密
     * @param key ByteArray 解密密码bytes
     * @param iv ByteArray 偏移量bytes
     * @param transformation String String -the name of the transformation, e.g., DES/CBC/PKCS5Padding. See the Cipher
     *  section in the Java Cryptography Architecture Standard Algorithm Name Documentation for information about standard transformation names.
     * @param message ByteArray 需要解密的数据bytes
     * @return ByteArray 解密后的数据bytes
     */
    fun decrypt(key: ByteArray, iv: ByteArray, transformation: String = TRANSFORMATION, message: ByteArray): ByteArray {
        val keySpec = SecretKeySpec(key, CHARSET)
        val ivParameterSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)
        return cipher.doFinal(message)
    }


    /**
     * 解密数据,加密后通过Base64编码返回
     * @param key 解密密码
     * @param iv 偏移量字符串
     * @param transformation String -the name of the transformation, e.g., DES/CBC/PKCS5Padding. See the Cipher
     * section in the Java Cryptography Architecture Standard Algorithm Name Documentation for information about standard transformation names.
     * @param base64EncodeMessage String  经过Base64 Encode后的加密数据
     * @return String? 加密后的数据,加密失败则返回null
     */
    fun decrypt(key: String, iv: String, transformation: String = TRANSFORMATION, base64EncodeMessage: String): String {
        val decodeString = Base64.decode(base64EncodeMessage, Base64.NO_WRAP)
        val resultByte = decrypt(
            stringToBytes(key),
            stringToBytes(iv),
            transformation,
            decodeString
        )
        return String(resultByte)
    }


    private fun bytesToString(byteArray: ByteArray): String {
        return byteArray.toHexString()
    }

    private fun stringToBytes(string: String): ByteArray {
        return string.hexStringToByteArray()
    }
}