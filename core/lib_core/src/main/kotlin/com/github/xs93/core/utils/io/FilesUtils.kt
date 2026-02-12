package com.github.xs93.core.utils.io

import android.os.Build
import java.io.File
import java.io.IOException
import java.nio.file.Files

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/12 16:22
 * @email 466911254@qq.com
 */
object FilesUtils {


    /**
     * 移动文件或文件夹
     * @param source File  可以是文件也可以是文件夹
     * @param target File  必须是文件夹
     * @param existJump Boolean 目标文件已经存在时，true表示跳过，false表示文件重命名（文件名加数字递增的方式）移动
     * @return 返回所有移动成功后的目标文件路径
     */
    @Throws(IOException::class)
    fun moveFile(source: File?, target: File?, existJump: Boolean): List<String>? {
        if (source == null || target == null) {
            return null
        }

        if (!target.exists()) {
            target.mkdirs()
        } else {
            if (!target.isDirectory) {
                throw IllegalArgumentException("target must is directory!!!")
            }
        }

        var sourcePath = source.path
        val targetPath = target.path
        val paths: Array<String>?
        if (source.isDirectory) {
            paths = source.list() ?: emptyArray()
        } else {
            sourcePath = source.parent ?: "/"
            paths = arrayOf(source.name)
        }

        val successList = mutableListOf<String>()

        paths.forEach {
            val tempFile = File(sourcePath, it)
            val newFile = File(targetPath, it)
            if (tempFile.isDirectory) {
                val middleList = moveFile(tempFile, newFile, existJump)
                if (middleList?.isNotEmpty() == true) {
                    successList.addAll(middleList)
                }
            } else {
                if (newFile.exists()) {
                    if (!existJump) {
                        var newTempFile: File
                        var i = 1
                        do {
                            val attr = it.split(".")
                            var temp = attr[0] + "($i)"
                            if (attr.size > 1) {
                                temp += ".${attr[1]}"
                            }
                            newTempFile = File(temp)
                            i++
                        } while (newFile.exists())
                        val successPath = moveFileCompat(tempFile, newTempFile)
                        if (!sourcePath.isNullOrBlank()) {
                            successList.add(successPath!!)
                        }
                    }
                } else {
                    val successPath = moveFileCompat(tempFile, newFile)
                    if (!sourcePath.isNullOrBlank()) {
                        successList.add(successPath!!)
                    }
                }
            }
        }
        source.deleteRecursively()
        return successList
    }

    /**
     * 移动文件，必须为文件
     * @param oldFile File 旧文件对象
     * @param newFile File 新文件对象
     * @return String?
     */
    private fun moveFileCompat(oldFile: File, newFile: File): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Files.move(oldFile.toPath(), newFile.toPath())
                return newFile.path
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                val renameSuccess = oldFile.renameTo(newFile)
                if (renameSuccess) {
                    return newFile.path
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }


    @Throws(IOException::class)
    private fun copyFile(
        source: File?,
        target: File?,
        existJump: Boolean,
        delete: Boolean
    ): List<String>? {
        if (source == null || target == null) {
            return null
        }
        if (!target.exists()) {
            target.mkdirs()
        } else {
            if (!target.isDirectory) {
                throw IllegalArgumentException("target must is directory!!!")
            }
        }

        var sourcePath = source.path
        val targetPath = target.path
        val paths: Array<String>?
        if (source.isDirectory) {
            paths = source.list() ?: emptyArray()
        } else {
            sourcePath = source.parent ?: "/"
            paths = arrayOf(source.name)
        }
        val successList = mutableListOf<String>()

        paths.forEach {
            val tempFile = File(sourcePath, it)
            val newFile = File(targetPath, it)
            if (tempFile.isDirectory) {
                val middleList = copyFile(tempFile, newFile, existJump, delete)
                if (middleList?.isNotEmpty() == true) {
                    successList.addAll(middleList)
                }
            } else {
                if (newFile.exists()) {
                    if (!existJump) {
                        var newTempFile: File
                        var i = 1
                        do {
                            val attr = it.split(".")
                            var temp = attr[0] + "($i)"
                            if (attr.size > 1) {
                                temp += ".${attr[1]}"
                            }
                            newTempFile = File(temp)
                            i++
                        } while (newFile.exists())
                        val successPath = copyFileCompat(tempFile, newTempFile)
                        if (!sourcePath.isNullOrBlank()) {
                            successList.add(successPath!!)
                            if (delete) {
                                deleteFile(tempFile)
                            }
                        }
                    }
                } else {
                    val successPath = moveFileCompat(tempFile, newFile)
                    if (!sourcePath.isNullOrBlank()) {
                        successList.add(successPath!!)
                    }
                }
            }
        }
        if (delete) {
            source.deleteRecursively()
        }
        return successList
    }

    /**
     * 移动文件，必须为文件
     * @param oldFile File 旧文件对象
     * @param newFile File 新文件对象
     * @return String?
     */
    private fun copyFileCompat(oldFile: File, newFile: File): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Files.copy(oldFile.toPath(), newFile.toPath())
                return newFile.path
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                oldFile.copyTo(newFile)
                return newFile.path
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }


    /**
     * 删除文件
     * @param file File? 需要删除的文件
     * @return Boolean 删除文件成功,设置的文件不存在
     */
    fun deleteFile(file: File?): Boolean {
        if (file == null || !file.exists()) {
            return true
        }
        return if (file.isFile) {
            file.delete()
        } else {
            file.deleteRecursively()
        }
    }
}