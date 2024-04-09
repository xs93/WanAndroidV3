package com.github.xs93.wanandroid.common.data

import com.github.xs93.wanandroid.common.account.AccountManager
import com.github.xs93.wanandroid.common.account.AccountState
import com.github.xs93.wanandroid.common.entity.User
import com.github.xs93.wanandroid.common.entity.UserDetailInfo
import com.github.xs93.wanandroid.common.network.WanResponse
import com.github.xs93.wanandroid.common.services.AccountService
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/8 14:07
 * @email 466911254@qq.com
 */


interface IAccount {

    val accountState: StateFlow<AccountState>

    val userDetailFlow: StateFlow<UserDetailInfo>

    val isLogin: Boolean

    val userId: Int

    suspend fun fetchUserInfo(): Result<WanResponse<UserDetailInfo>>

    suspend fun login(username: String, password: String): Result<WanResponse<User>>

    suspend fun logout(): Result<WanResponse<Int>>

    suspend fun register(username: String, password: String, confirmPassword: String): Result<WanResponse<Nothing>>
}

class AccountDataModule @Inject constructor(
    private val accountService: AccountService,
    private val accountManager: AccountManager
) : IAccount {
    override val accountState: StateFlow<AccountState>
        get() = accountManager.accountStateFlow
    override val userDetailFlow: StateFlow<UserDetailInfo>
        get() = accountManager.userDetailFlow
    override val isLogin: Boolean
        get() = accountManager.isLogin
    override val userId: Int
        get() = accountManager.userId

    override suspend fun fetchUserInfo(): Result<WanResponse<UserDetailInfo>> {
        val result = accountService.fetchUserInfo()
        result.onSuccess {
            val userDetailInfo = it.data
            if (userDetailInfo != null) {
                accountManager.cacheUserDetailInfo(userDetailInfo)
            }
        }
        return result
    }

    override suspend fun login(username: String, password: String): Result<WanResponse<User>> {
        val result = accountService.login(username, password)
        result.onSuccess {
            val user = it.data
            if (user != null) {
                accountManager.logIn(user)
            }
        }
        return result
    }

    override suspend fun logout(): Result<WanResponse<Int>> {
        val result = accountService.logout()
        result.onSuccess {
            accountManager.logout()
        }
        return result
    }

    override suspend fun register(
        username: String,
        password: String,
        confirmPassword: String
    ): Result<WanResponse<Nothing>> {
        return accountService.register(username, password, confirmPassword)
    }
}