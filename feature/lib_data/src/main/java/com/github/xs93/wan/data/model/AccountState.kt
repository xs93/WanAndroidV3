package com.github.xs93.wan.data.model

import com.github.xs93.wan.data.entity.User

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

    data class LogIn(val user: User, val fromCache: Boolean) : AccountState()
}

internal val AccountState.isLogin: Boolean
    get() = this is AccountState.LogIn