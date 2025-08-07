package com.github.xs93.wanandroid.common.network

import com.github.xs93.network.model.ApiResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *
 * WanAndroid 返回数据结构Response
 *
 * @author xushuai
 * @date   2022/9/7-14:42
 * @email  466911254@qq.com
 */

@JsonClass(generateAdapter = true)
data class WanResponse<out T>(
    @param:Json(name = "errorCode")
    override val errorCode: Int,
    @param:Json(name = "errorMsg")
    override val errorMessage: String,
    @param:Json(name = "data")
    override val data: T? = null,
) : ApiResponse<T>(errorCode, errorMessage, data) {
    companion object {
        const val ERROR_NOT_LOGIN = -1001
    }

    override fun isNotLogin(): Boolean {
        return errorCode == ERROR_NOT_LOGIN
    }
}