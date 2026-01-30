package com.github.xs93.wan.common.account

import com.github.xs93.wan.common.entity.User

/**
 * 账号登录状态
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 9:38
 * @email 466911254@qq.com
 */
sealed class AccountState {
    data object LogOut : AccountState()

    data class LogIn(val fromCache: Boolean, val user: User) : AccountState()
}

internal val AccountState.isLogin: Boolean
    get() = this is AccountState.LogIn