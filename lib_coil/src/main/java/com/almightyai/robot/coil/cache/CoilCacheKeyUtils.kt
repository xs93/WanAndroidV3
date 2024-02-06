package com.almightyai.robot.coil.cache

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/4/7 11:33
 * @email 466911254@qq.com
 */
class CoilCacheKeyUtils {

    companion object {
        fun getKey(path: String): String {
            return if (path.startsWith("https://aiwriting.s3.us-east-2.amazonaws.com/") ||
                path.startsWith("https://wh-aichatbot.s3.us-east-2.amazonaws.com")
            ) {
                val tokenStart = path.indexOf("?")
                if (tokenStart == -1) {
                    path
                } else {
                    val newData = path.substring(0, tokenStart)
                    newData
                }
            } else {
                path
            }
        }
    }
}