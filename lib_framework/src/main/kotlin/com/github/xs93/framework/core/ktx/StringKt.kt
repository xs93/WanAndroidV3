package com.github.xs93.framework.core.ktx

/**
 * String相关扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/2/14 11:32
 * @email 466911254@qq.com
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