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

    data object Success : PageLoadStatus(0)

    data object Loading : PageLoadStatus(1)

    data object Empty : PageLoadStatus(2)

    data object Failed : PageLoadStatus(3)

    data object NoNetwork : PageLoadStatus(4)

    data class Other(val otherStatus: Int) : PageLoadStatus(status = otherStatus)
}
