package com.github.xs93.wanandroid.login

import com.github.xs93.wanandroid.common.model.AccountModel
import com.github.xs93.wanandroid.common.model.WanResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *
 * 登录相关接口
 *
 * @author xushuai
 * @date   2022/9/7-14:30
 * @email  466911254@qq.com
 */
interface LoginApi {

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String,
    ): WanResponse<AccountModel>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): WanResponse<AccountModel>

    @GET("user/logout/json")
    suspend fun logout(): WanResponse<Any>
}