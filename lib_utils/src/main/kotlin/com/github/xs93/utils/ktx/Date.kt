package com.github.xs93.utils.ktx

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 日期相关的扩展函数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 10:13
 * @email 466911254@qq.com
 */

/**
 * 时间戳转换为倒计时时间
 * @receiver Long 毫秒时间戳
 * @return String 倒计时时间字符串
 */
fun Long.toCountDownTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("GMT+00:00")
    return formatter.format(this)
}

/**
 * 时间戳转为时间字符串
 * @receiver Long 时间戳,毫秒
 * @param pattern String 时间字符串格式
 * @param locale Locale  地区
 * @param timeZone TimeZone? 时区
 * @return String 时间字符串，当转换失败,返回空字符串
 */
fun Long.formatDateTime(
    pattern: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone? = null
): String {
    val simpleFormatter = SimpleDateFormat(pattern, locale)
    if (timeZone != null) {
        simpleFormatter.timeZone = timeZone
    }
    return try {
        simpleFormatter.format(Date(this))
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * 判断 2个时间戳是否是同一天
 * @receiver Long 时间戳
 * @param timestamp Long 目标时间戳
 * @param timeZone TimeZone 时区
 * @return Boolean true 是同一天,false，不是同一天
 */
fun Long.isSameDay(timestamp: Long, timeZone: TimeZone): Boolean {
    val calendar1 = Calendar.getInstance(timeZone)
    val calendar2 = Calendar.getInstance(timeZone)
    calendar1.timeInMillis = this
    calendar2.timeInMillis = timestamp
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
            calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
}

/**
 * 判断当前时间戳是否是今天
 * @receiver Long 时间戳
 * @return Boolean true，时间是今天,false 不是今天
 */
fun Long.isToday(): Boolean {
    return isSameDay(System.currentTimeMillis(), TimeZone.getDefault())
}

/**
 * 时间字符串 转换为时间戳
 *
 * @receiver String 日期时间字符串
 * @param pattern String 日期时间字符串格式
 * @param locale Locale 地区
 * @param timeZone TimeZone? 时区
 * @return Long 时间字符串,转换失败，则返回0
 */
fun String.parseDateTime(
    pattern: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone? = null
): Long {
    val simpleFormatter = SimpleDateFormat(pattern, locale)
    if (timeZone != null) {
        simpleFormatter.timeZone = timeZone
    }
    try {
        val date = simpleFormatter.parse(this)
        return date?.time ?: 0L
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0L
}

