package com.github.xs93.wan.data.respotory

import com.github.xs93.core.ktx.runSuspendCatching
import com.github.xs93.wan.data.api.AccountApi
import com.github.xs93.wan.data.entity.User
import com.github.xs93.wan.data.entity.UserDetailInfo
import com.github.xs93.wan.data.model.WanResponse
import com.github.xs93.wan.data.usercase.AccountDataManager
import javax.inject.Inject

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/8
 * @description 账号数据仓库
 *
 */
class AccountRepository @Inject constructor(
    private val accountApi: AccountApi,
    private val accountDataManager: AccountDataManager
) {
    suspend fun login(username: String, password: String): Result<WanResponse<User>> {
        return runSuspendCatching { accountApi.login(username, password) }
            .onSuccess {
                val user = it.data
                if (user != null) {
                    accountDataManager.saveUserInfo(user)
                }
            }
    }

    suspend fun logout(): Result<WanResponse<Int>> {
        return runSuspendCatching { accountApi.logout() }
            .onSuccess {
                accountDataManager.clearUserInfo()
            }
    }

    suspend fun register(
        username: String,
        password: String,
        confirmPassword: String
    ): Result<WanResponse<Nothing>> {
        return runSuspendCatching { accountApi.register(username, password, confirmPassword) }
    }

    suspend fun fetchUserInfo(): Result<WanResponse<UserDetailInfo>> {
        return runSuspendCatching { accountApi.fetchUserInfo() }
            .onSuccess {
                val userDetailInfo = it.data
                if (userDetailInfo != null) {
                    accountDataManager.saveUserDetailInfo(userDetailInfo)
                }
            }
    }
}