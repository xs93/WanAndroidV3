package com.github.xs93.wan.model.model

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
    val errorCode: Int,
    @param:Json(name = "errorMsg")
    val errorMessage: String,
    @param:Json(name = "data")
    val data: T? = null,
) {
    companion object {
        const val SUCCESS_CODE = 0
        const val ERROR_NOT_LOGIN = -1001
    }

    fun isSuccess(): Boolean {
        return errorCode == SUCCESS_CODE
    }

    fun isFailed(): Boolean {
        return errorCode != SUCCESS_CODE
    }

    fun isNotLogin(): Boolean {
        return errorCode == ERROR_NOT_LOGIN
    }
}