package com.github.xs93.framework.core.ui

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/19 15:56
 * @email 466911254@qq.com
 */
data class Insets(val start: Int, val top: Int, val end: Int, val bottom: Int) {
    companion object {
        val EMPTY = Insets(0, 0, 0, 0)
    }
}
