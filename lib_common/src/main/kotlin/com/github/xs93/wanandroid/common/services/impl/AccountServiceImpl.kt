package com.github.xs93.wanandroid.common.services.impl

import com.github.xs93.network.EasyRetrofit
import com.github.xs93.wanandroid.AppConstant
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

    override suspend fun login(username: String, password: String): WanResponse<User> =
        service.login(username, password)

    override suspend fun logout(): WanResponse<Nothing> = service.logout()

    override suspend fun register(username: String, password: String, confirmPassword: String): WanResponse<Nothing> =
        service.register(username, password, confirmPassword)

    override suspend fun getUserInfo(): WanResponse<UserDetailInfo> = service.getUserInfo()
}