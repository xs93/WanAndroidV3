package com.github.xs93.wanandroid.web.kts

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/4 14:21
 * @email 466911254@qq.com
 */


fun InputStream.String(): String {
    val reader = BufferedReader(InputStreamReader(this, "utf-8"))
    val sb = StringBuilder()
    var s: String? = reader.readLine()
    while (s != null) {
        sb.append(s).append("\n")
        s = reader.readLine()
    }
    return sb.toString()
}