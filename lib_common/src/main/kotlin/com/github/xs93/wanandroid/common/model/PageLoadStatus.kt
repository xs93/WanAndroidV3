package com.github.xs93.wanandroid.common.model

/**
 * 页面加载状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/21 15:19
 * @email 466911254@qq.com
 */
sealed class PageLoadStatus {
    object Loading : PageLoadStatus()

    object Success : PageLoadStatus()

    object Failed : PageLoadStatus()

    object NoNetwork : PageLoadStatus()
}
