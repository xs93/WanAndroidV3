package com.github.xs93.wanandroid.common.services.impl

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wanandroid.AppConstant
import com.github.xs93.wanandroid.common.account.AccountManager
import com.github.xs93.wanandroid.common.entity.User
import com.github.xs93.wanandroid.common.entity.UserDetailInfo
import com.github.xs93.wanandroid.common.network.WanResponse
import com.github.xs93.wanandroid.common.services.AccountService
import javax.inject.Inject

/**
 * 账号接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/3 16:41
 * @email 466911254@qq.com
 */
class AccountServiceImpl @Inject constructor() : AccountService {

    private val service by lazy { EasyRetrofit.create(AppConstant.BaseUrl, service = AccountService::class.java) }

    @Inject
    lateinit var accountManager: AccountManager


    override suspend fun login(username: String, password: String): Result<WanResponse<User>> {
        val result = service.login(username, password)
        result.onSuccess {
            val user = it.data
            if (user != null) {
                accountManager.logIn(user)
            }
        }
        return result
    }

    override suspend fun logout(): Result<WanResponse<Int>> {
        val result = service.logout()
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
        return service.register(username, password, confirmPassword)
    }

    override suspend fun fetchUserInfo(): Result<WanResponse<UserDetailInfo>> {
        val result = service.fetchUserInfo()
        result.onSuccess {
            val userDetailInfo = it.data
            if (userDetailInfo != null) {
                accountManager.cacheUserDetailInfo(userDetailInfo)
            }
        }
        return result
    }
}