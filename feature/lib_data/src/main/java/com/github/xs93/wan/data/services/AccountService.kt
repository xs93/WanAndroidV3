package com.github.xs93.wan.data.services

import com.github.xs93.wan.data.entity.User
import com.github.xs93.wan.data.entity.UserDetailInfo
import com.github.xs93.wan.data.model.WanResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/3 16:26
 * @email 466911254@qq.com
 */
interface AccountService {

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
        @Field("password") password: String,
    ): Result<WanResponse<User>>

    /**
     * 账号登出
     * @return WanResponse<Any>
     */
    @GET("user/logout/json")
    suspend fun logout(): Result<WanResponse<Int>>

    /**
     * 注册
     * @param username String 用户名
     * @param password String 密码
     * @param confirmPassword String 确认密码
     * @return WanResponse<Nothing>
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") confirmPassword: String,
    ): Result<WanResponse<Nothing>>


    /**
     * 获取用户信息
     */
    @GET("user/lg/userinfo/json")
    suspend fun fetchUserInfo(): Result<WanResponse<UserDetailInfo>>
}