package com.github.xs93.wanandroid.common.model

/**
 * 页面加载状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/21 15:19
 * @email 466911254@qq.com
 */
sealed class PageLoadStatus(val status: Int) {
    object Success : PageLoadStatus(0)

    object Loading : PageLoadStatus(1)

    object Empty : PageLoadStatus(2)

    object Failed : PageLoadStatus(3)

    object NoNetwork : PageLoadStatus(4)

    data class Other(val otherStatus: Int) : PageLoadStatus(status = otherStatus)
}
