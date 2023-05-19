package com.github.xs93.framework.network.model

import com.github.xs93.framework.network.exception.ServiceException

/**
 *
 * 默认的基础数据结构
 *
 * @author xushuai
 * @date   2022/9/2-14:32
 * @email  466911254@qq.com
 */
open class ApiResponse<out T>(
    val errorCode: Int = -1,
    val errorMessage: String = "default error code",
    val data: T? = null,
) {

    companion object {
        const val SUCCESS_CODE = 0
    }

    open fun isSuccess(): Boolean {
        return errorCode == SUCCESS_CODE
    }

    open fun coverData(): T? {
        if (isSuccess()) {
            return data
        }
        throw ServiceException(errorCode, errorMessage)
    }
}