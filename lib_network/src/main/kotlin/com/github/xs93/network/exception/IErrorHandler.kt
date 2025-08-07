package com.github.xs93.network.exception

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/7 14:19
 * @description 全局网络错误处理接口
 *
 */
interface IErrorHandler {

    fun handleError(e: Throwable)

    fun handleNetworkError(e: NetworkException)

    fun handleResponseError(e: ResponseException)

    fun handleConversionError(e: ConversionException)

    @Throws(ApiException::class)
    fun checkApiServiceError(any: Any?)

    fun handleApiServiceError(e: ApiException)
}