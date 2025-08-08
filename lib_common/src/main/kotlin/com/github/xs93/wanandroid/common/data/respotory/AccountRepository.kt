package com.github.xs93.wanandroid.common.data.respotory

import com.github.xs93.network.base.repository.BaseRepository
import com.github.xs93.wanandroid.common.account.AccountDataManager
import com.github.xs93.wanandroid.common.data.services.AccountService
import com.github.xs93.wanandroid.common.entity.User
import com.github.xs93.wanandroid.common.entity.UserDetailInfo
import com.github.xs93.wanandroid.common.network.WanResponse
import javax.inject.Inject

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description 账号数据仓库
 *
 */
class AccountRepository @Inject constructor(
    private val accountService: AccountService,
    private val accountDataManager: AccountDataManager
) : BaseRepository() {
    suspend fun login(username: String, password: String): Result<WanResponse<User>> {
        val result = accountService.login(username, password)
        result.onSuccess {
            val user = it.data
            if (user != null) {
                accountDataManager.saveUserInfo(user)
            }
        }
        return result
    }

    suspend fun logout(): Result<WanResponse<Int>> {
        val result = accountService.logout()
        result.onSuccess {
            accountDataManager.clearUserInfo()
        }
        return result
    }

    suspend fun register(
        username: String,
        password: String,
        confirmPassword: String
    ): Result<WanResponse<Nothing>> {
        return accountService.register(username, password, confirmPassword)
    }

    suspend fun fetchUserInfo(): Result<WanResponse<UserDetailInfo>> {
        val result = accountService.fetchUserInfo()
        result.onSuccess {
            val userDetailInfo = it.data
            if (userDetailInfo != null) {
                accountDataManager.saveUserDetailInfo(userDetailInfo)
            }
        }
        return result
    }
}