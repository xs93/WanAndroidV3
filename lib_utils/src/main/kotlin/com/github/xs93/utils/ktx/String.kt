package com.github.xs93.utils.ktx

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.content.ContextCompat

/**
 * Html格式化方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 17:06
 * @email 466911254@qq.com
 */

/**
 * 生成随机字符串
 * @param length Int 生成字符串长度
 * @param minLength Int 当length <0时，随机字符串最小长度
 * @param maxLength Int 当length <0时，随机字符串最大长度
 * @return String 生成字符串结果
 */
fun randomString(length: Int = -1, minLength: Int = 1, maxLength: Int = 30): String {
    val dictCharts = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789"
    return StringBuilder().apply {
        if (length <= 0) {
            (1..(minLength..maxLength).random()).onEach {
                append(dictCharts.random())
            }
        } else {
            (1..length).onEach {
                append(dictCharts.random())
            }
        }
    }.toString()
}

fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}

/**
 * 字符串复制到剪切板
 * @receiver String 复制对象
 * @param context Context 上下文对象
 * @param label String 剪切板label
 * @return Boolean 复制结果,true 复制成功,false 复制失败
 */
fun String.copyToClipboard(context: Context, label: String = "label"): Boolean {
    return try {
        val cm: ClipboardManager =
            ContextCompat.getSystemService(context, ClipboardManager::class.java) ?: return false
        val clipData = ClipData.newPlainText(label, this)
        cm.setPrimaryClip(clipData)
        true
    } catch (e: Exception) {
        false
    }
}
