package com.github.xs93.framework.network.exception

/**
 * 统一个请求错误信息
 *
 *
 * @author xushuai
 * @date   2022/9/2-14:57
 * @email  466911254@qq.com
 */
@Suppress("MemberVisibilityCanBePrivate")
class ResponseException(
    val errorCode: Int,
    val errorMessage: String? = "",
    throwable: Throwable,
) : Exception(errorMessage, throwable) {
    companion object {
        private const val serialVersionUID = -113L
    }

    override fun toString(): String {
        return "errorCode = $errorCode,errorMessage = $errorMessage,$cause"
    }
}