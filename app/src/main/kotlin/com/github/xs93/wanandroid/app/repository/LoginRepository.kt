package com.github.xs93.wanandroid.app.repository

import com.github.xs93.network.base.repository.BaseRepository
import com.github.xs93.wanandroid.app.api.UserService
import com.github.xs93.wanandroid.app.entity.AccountInfo
import com.github.xs93.wanandroid.common.network.WanResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * 登录相关数据仓库
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/7 14:36
 * @email 466911254@qq.com
 */
@ActivityRetainedScoped
class LoginRepository @Inject constructor(private val userService: UserService) : BaseRepository() {


    /**
     * 用户登录
     * @param username String 账号信息
     * @param password String 密码信息
     * @return WanResponse<AccountInfo>? 返回信息
     */
    suspend fun login(username: String, password: String): Result<WanResponse<AccountInfo>> {
        return runSafeSuspendCatching {
            userService.login(username, password)
        }
    }
}