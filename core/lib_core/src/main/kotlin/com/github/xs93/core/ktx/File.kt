package com.github.xs93.core.ktx

import java.io.File

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/6 15:44
 * @email 466911254@qq.com
 */


/**
 * 如果文件件不存在则创建，存在则判断是否是文件件类型
 * @receiver File
 * @return Boolean
 */
fun File?.createOrExistsDir(): Boolean {
    return this != null && if (exists()) isDirectory else mkdirs()
}

/**
 * 创建文件，如果有旧文件则删除，然后创建新的文件
 * @receiver File?
 * @return Boolean
 */
fun File?.createFileByDeleteOldFile(): Boolean {
    if (this == null) return false
    if (exists() && !delete()) return false
    if (parentFile.createOrExistsDir()) return false
    return try {
        createNewFile()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}