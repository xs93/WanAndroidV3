package com.github.xs93.network.model

/**
 *
 * 默认的基础数据结构
 *
 * @author xushuai
 * @date   2022/9/2-14:32
 * @email  466911254@qq.com
 */
open class ApiResponse<out T>(
    open val errorCode: Int = -1,
    open val errorMessage: String = "default error code",
    open val data: T? = null,
) {

    companion object {

        const val SUCCESS_CODE = 0
    }

    open fun isSuccess(): Boolean {
        return errorCode == SUCCESS_CODE
    }

    open fun isFailed(): Boolean {
        return errorCode != SUCCESS_CODE
    }

    open fun isNotLogin(): Boolean {
        return false
    }
}