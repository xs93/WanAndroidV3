package com.github.xs93.wanandroid.app.api

import com.github.xs93.wanandroid.app.entity.AccountInfo
import com.github.xs93.wanandroid.common.network.WanResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 登录用户相关接口
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/7 14:38
 * @email 466911254@qq.com
 */
interface UserService {

    /**
     * 用户登录
     * @param username String 用户账号
     * @param password String 用户密码
     * @return WanResponse<AccountInfo> 登录用户信息
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): WanResponse<AccountInfo>
}