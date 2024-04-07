package com.github.xs93.wanandroid.common.account

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/3 16:16
 * @email 466911254@qq.com
 */
sealed class AccountState {
    data class LogIn(val isFromCookie: Boolean)
}