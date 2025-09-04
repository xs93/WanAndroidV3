package com.github.xs93.wan.common.model

/**
 * 页面加载状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/21 15:19
 * @email 466911254@qq.com
 */
sealed class PageStatus(val status: Int) {

    data object Success : PageStatus(0)

    data object Loading : PageStatus(1)

    data object Empty : PageStatus(2)

    data object Failed : PageStatus(3)

    data object NoNetwork : PageStatus(4)

    data class Other(val otherStatus: Int) : PageStatus(status = otherStatus)
}
