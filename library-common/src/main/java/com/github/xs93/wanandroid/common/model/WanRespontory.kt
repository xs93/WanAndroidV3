package com.github.xs93.wanandroid.common.model

import com.github.xs93.retrofit.exception.ServiceException
import com.github.xs93.retrofit.model.ApiResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *
 *WanAndroid 返回数据结构Response
 *
 * @author xushuai
 * @date   2022/9/7-14:42
 * @email  466911254@qq.com
 */
@JsonClass(generateAdapter = true)
class WanResponse<out T>(
    @Json(name = "errorCode")
    errorCode: Int,
    @Json(name = "errorMsg")
    errorMessage: String,
    @Json(name = "data")
    data: T? = null,
) : ApiResponse<T>(errorCode, errorMessage, data) {
    companion object {
        const val ERROR_NOT_LOGIN = -1001
    }

    override fun coverData(): T? {
        if (isSuccess()) return data
        if (errorCode == ERROR_NOT_LOGIN) {
            throw ServiceException(errorCode, errorMessage)
        }
        throw ServiceException(errorCode, errorMessage)
    }
}