package com.github.xs93.framework.network.exception

/**
 *
 * 接口请求错误
 *
 * @author xushuai
 * @date   2022/9/2-14:33
 * @email  466911254@qq.com
 */
open class ApiException(
    val errorCode: Int,
    val errorMessage: String? = "",
    throwable: Throwable? = null,
) : Exception(errorMessage, throwable) {
    companion object {
        private const val serialVersionUID = -4966800L
    }

    override fun toString(): String {
        return "errorCode = $errorCode,errorMessage = $errorMessage,$cause"
    }
}