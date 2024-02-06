package com.github.xs93.utils.net

import androidx.annotation.Keep
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URI
import java.util.regex.Pattern

/**
 * 使用Ping命令并且解析出结果，最好在子线程执行
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/18 17:00
 * @email 466911254@qq.com
 */
@Suppress("unused")
class PingUtils {

    companion object {
        private const val ipRegex =
            "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))"

        @JvmStatic
        fun ping(url: String, count: Int = 3, timeOut: Int = 10): PingResult {
            val domain = getDomain(url) ?: return PingResult(url, "", false, -1, count, timeOut)
            val result = execCommand(createSimplePingCommand(count, timeOut, domain))
            val status = result[0].toInt()
            val connect = status == 0
            val pingString = result[1]
            if (pingString.isBlank() && status != 0) {
                return PingResult(url, domain, false, status, count, timeOut)
            }
            try {
                // 解析丢包率
                var tempInfo = pingString.substring(pingString.indexOf("received,"))
                tempInfo = tempInfo.substring(9, tempInfo.indexOf("packet")).trim()
                val lost = tempInfo.replace("%", "").toInt()

                // 解析延迟信息,获取以"min/avg/tempInfo/mdev"为头的文本，分别获取此次的ping参数
                return if (pingString.contains("min/avg/max/mdev")) {
                    tempInfo = pingString.substring(pingString.indexOf("min/avg/max/mdev") + 19)
                    tempInfo = tempInfo.substring(0, tempInfo.indexOf("ms")).trim()
                    val temps = tempInfo.split("/").toTypedArray()
                    val min = temps[0].toFloat()
                    val avg = temps[1].toFloat()
                    val max = temps[2].toFloat()
                    val mdev = temps[3].toFloat()
                    PingResult(url, domain, connect, status, count, timeOut, lost, min, avg, max, mdev)
                } else {
                    PingResult(url, domain, connect, status, count, timeOut, lost)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return PingResult(url, domain, connect, status, count, timeOut)
        }

        /**
         * 判断字符串是否是ip地址
         * @param string String
         * @return Boolean true，输入字符串是IP地址
         */
        @JvmStatic
        fun isIp(string: String): Boolean {
            return Pattern.matches(ipRegex, string)
        }

        /**
         * 域名获取
         * @param url String 网站
         * @return String? 获取到的域名
         */
        private fun getDomain(url: String): String? {
            var domain: String? = null
            try {
                domain = URI.create(url).host
                if (domain == null && isIp(url)) {
                    domain = url
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return domain
        }


        private fun createSimplePingCommand(count: Int, timeOut: Int, domain: String): String {
            return "/system/bin/ping -c $count -w $timeOut $domain"
        }

        private fun execCommand(command: String): Array<String> {
            var process: Process? = null
            var status = -1
            var result = ""
            try {
                Runtime.getRuntime().exec(command).also {
                    process = it
                    val inputStream: InputStream = it.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val sb = StringBuilder()
                    var line: String?
                    while (null != reader.readLine().also { readLine -> line = readLine }) {
                        sb.append(line)
                        sb.append("\n")
                    }
                    status = it.waitFor()
                    reader.close()
                    inputStream.close()
                    result = sb.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                process?.destroy()
            }
            return arrayOf(status.toString(), result)
        }
    }
}

@Keep
data class PingResult(
    val url: String = "",
    val ip: String = "",
    val connect: Boolean = false,
    val status: Int = -1,
    val count: Int = 0,
    val timeOut: Int = -1,
    val lost: Int = 100,
    val min: Float = -1f,
    val avg: Float = -1f,
    val max: Float = -1f,
    val mdev: Float = -1f
)