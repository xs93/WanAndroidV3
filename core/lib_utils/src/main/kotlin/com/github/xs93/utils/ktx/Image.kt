@file:JvmName("ImageUtils")

package com.github.xs93.utils.ktx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.annotation.IntRange
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/6 14:19
 * @email 466911254@qq.com
 */


/**
 * 加载图片时，计算图片加载尺寸缩放  inSampleSize,防止图片过大发生OOM
 * @param options BitmapFactory.Options
 * @param reqWidth Int
 * @param reqHeight Int
 * @return Int
 */
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    var inSampleSize = 1
    val width = options.outWidth
    val height = options.outHeight
    if (width > reqWidth || height > reqWidth) {
        val halfWidth = width / 2
        val halfHeight = height / 2
        while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

/**
 * 通过uri 获取指定尺寸大小的的bitmap
 * @receiver Uri
 * @param context Context 上下文对象
 * @param reqWidth Int 指定图片宽度
 * @param reqHeight Int 指定图片高度
 * @return Bitmap? Bitmap对象
 */
fun Uri.decodeBitmapByInSampleSize(context: Context, reqWidth: Int, reqHeight: Int): Bitmap? {
    var inputStream: InputStream? = null
    try {
        inputStream = context.contentResolver.openInputStream(this)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        inputStream.safeClose(true)

        inputStream = context.contentResolver.openInputStream(this)
        return BitmapFactory.decodeStream(inputStream, null, options)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        inputStream.safeClose(true)
    }
    return null
}


/**
 * url 压缩图片
 * @receiver Uri
 * @param context Context
 * @param reqWidth Int
 * @param reqHeight Int
 * @param maxByteSize Long
 * @return ByteArray
 */
fun Uri.imageCompress(context: Context, reqWidth: Int, reqHeight: Int, maxByteSize: Long): ByteArray? {
    val bitmap = decodeBitmapByInSampleSize(context, reqWidth, reqHeight)
    return bitmap?.compressByQuality(maxByteSize, true)
}

/**
 * 通过文件对象获取指定尺寸的Bitmap对象
 *
 * @receiver File 图片文件对象
 * @param reqWidth Int 指定图片宽度
 * @param reqHeight Int 指定图片高度
 * @return Bitmap? Bitmap对象
 */
fun File.decodeBitmapByInSampleSize(reqWidth: Int, reqHeight: Int): Bitmap? {
    var inputStream: InputStream? = null
    try {
        inputStream = FileInputStream(this)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        inputStream.safeClose(true)

        inputStream = FileInputStream(this)
        return BitmapFactory.decodeStream(inputStream, null, options)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        inputStream.safeClose(true)
    }
    return null
}


/**
 * File 文件压缩图片
 * @receiver Uri
 * @param reqWidth Int
 * @param reqHeight Int
 * @param maxByteSize Long
 * @return ByteArray
 */
fun File.imageCompress(reqWidth: Int, reqHeight: Int, maxByteSize: Long): ByteArray? {
    val bitmap = decodeBitmapByInSampleSize(reqWidth, reqHeight)
    return bitmap?.compressByQuality(maxByteSize, true)
}

fun Bitmap.isEmpty(): Boolean {
    return width == 0 || height == 0
}

/**
 * Bitmap 转ByteArray数组
 * @receiver Bitmap
 * @param format Bitmap.CompressFormat 转换格式
 * @param quality Int 压缩质量
 * @return ByteArray 转换后的ByteArray
 */
fun Bitmap.toByteArray(format: CompressFormat = CompressFormat.PNG, quality: Int = 100): ByteArray {
    val baos = ByteArrayOutputStream()
    compress(format, quality, baos)
    return baos.toByteArray()
}

/**
 * Drawable 对象转换成Bitmap 对象
 * @receiver Drawable
 * @return Bitmap
 */
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        if (bitmap != null) return bitmap
    }

    val bitmap: Bitmap

    @Suppress("DEPRECATION")
    val config = if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565

    bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        createBitmap(1, 1, config)
    } else {
        createBitmap(intrinsicWidth, intrinsicHeight, config)
    }
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

/**
 * 图片压缩到指定质量
 * @receiver Bitmap 原图片信息
 * @param quality Int 压缩质量
 * @return Bitmap 新的Bitmap
 */
fun Bitmap.compressByQuality(@IntRange(from = 0, to = 100) quality: Int, recycle: Boolean): ByteArray {
    val outputStream = ByteArrayOutputStream()
    compress(CompressFormat.JPEG, quality, outputStream)
    val bytes = outputStream.toByteArray()
    if (recycle && !isRecycled) {
        recycle()
    }
    return bytes
}

/**
 * 压缩Bitmap到指定的byte大小
 * @receiver Bitmap
 * @param maxByteSize Long 最大byte长度
 * @param recycle Boolean 是否回收
 * @return ByteArray 压缩后数组大小
 */
fun Bitmap.compressByQuality(maxByteSize: Long, recycle: Boolean): ByteArray {
    if (maxByteSize <= 0) return ByteArray(0)
    val baos = ByteArrayOutputStream()
    compress(CompressFormat.JPEG, 100, baos)
    val byteArray: ByteArray
    if (baos.size() <= maxByteSize) {
        byteArray = baos.toByteArray()
    } else {
        baos.reset()
        compress(CompressFormat.JPEG, 0, baos)
        if (baos.size() >= maxByteSize) {
            byteArray = baos.toByteArray()
        } else {
            // find the best quality using binary search
            var st = 0
            var end = 100
            var mid = 0

            while (st < end) {
                mid = (st + end) / 2
                baos.reset()
                compress(CompressFormat.JPEG, mid, baos)
                val len = baos.size().toLong()
                if (len == maxByteSize) {
                    break
                } else if (len > maxByteSize) {
                    end = mid - 1
                } else {
                    st = mid + 1
                }
            }
            if (end == mid - 1) {
                baos.reset()
                compress(CompressFormat.JPEG, st, baos)
            }
            byteArray = baos.toByteArray()
        }
    }
    if (recycle && !isRecycled) recycle()
    return byteArray
}


/**
 * 保存图片到指定的文件路径
 * @receiver Bitmap
 * @param path String 文件路径
 * @param format Bitmap.CompressFormat 保存压缩格式
 * @param quality Int 压缩质量
 * @param recycle Boolean 否释放Bitmap对象
 *   @return Boolean true 保存成功,false 保存失败
 */
fun Bitmap.save2FilePath(
    path: String,
    format: CompressFormat = CompressFormat.PNG,
    @IntRange(from = 0, to = 100) quality: Int = 100,
    recycle: Boolean = false
): Boolean {
    val file = File(path)
    return save2File(file, format, quality, recycle)
}

/**
 * 保存Bitmap 到指定文件
 * @receiver Bitmap
 * @param file File 指定文件对象
 * @param format Bitmap.CompressFormat 保存压缩格式
 * @param quality Int 压缩质量
 * @param recycle Boolean 是否释放Bitmap对象
 * @return Boolean true 保存成功,false 保存失败
 */
fun Bitmap.save2File(
    file: File,
    format: CompressFormat = CompressFormat.PNG,
    @IntRange(from = 0, to = 100) quality: Int = 100,
    recycle: Boolean = false
): Boolean {
    if (isEmpty()) {
        Log.e("ImageUtils", "bitmap is empty")
        return false
    }
    if (isRecycled) {
        Log.e("ImageUtils", "bitmap is recycled")
        return false
    }

    if (!file.createFileByDeleteOldFile()) {
        Log.e("ImageUtils", "createFileByDeleteOldFile is failed")
        return false
    }

    var os: OutputStream? = null
    var ret = false
    try {
        os = FileOutputStream(file)
        ret = compress(format, quality, os)
        if (recycle && !isRecycled) recycle()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        os.safeClose()
    }
    return ret
}