package com.github.xs93.framework.ui

/**
 * 内容Padding
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 15:56
 * @email 466911254@qq.com
 */
data class ContentPadding(val start: Int, val top: Int, val end: Int, val bottom: Int) {
    companion object {
        val EMPTY = ContentPadding(0, 0, 0, 0)
    }
}
