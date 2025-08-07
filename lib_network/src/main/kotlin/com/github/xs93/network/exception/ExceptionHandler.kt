package com.github.xs93.network.exception

import com.github.xs93.network.EasyRetrofit
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 *
 * 错误处理引擎
 *
 * @author xushuai
 * @date   2022/9/2-15:00
 * @email  466911254@qq.com
 */
object ExceptionHandler {
    /**
     * 网络连接异常处理错误
     */
    @JvmStatic
    fun handleNetworkException(e: IOException): NetworkException {
        val ex: NetworkException
        when (e) {
            is ConnectException -> {
                ex = NetworkException(ERROR.NETWORK_ERROR_CONNECT, e)
            }

            is SSLException -> {
                ex = NetworkException(ERROR.NETWORK_ERROR_SSL, e)
            }

            is SocketTimeoutException -> {
                ex = NetworkException(ERROR.NETWORK_ERROR_TIMEOUT, e)
            }

            is UnknownHostException -> {
                ex = NetworkException(ERROR.NETWORK_ERROR_UNKNOWN_HOST, e)
            }

            is NetworkException -> {
                ex = e
            }

            else -> {
                ex = NetworkException(ERROR.NETWORK_ERROR_UNKNOWN, e)
            }
        }
        callErrorHandler { handleError(ex) }
        callErrorHandler { handleNetworkError(ex) }
        return ex
    }

    /**
     * 响应错误处理错误
     */
    @JvmStatic
    fun handleErrorResponse(response: Response): ResponseException {
        val ex: ResponseException
        val errorCode = response.code
        when (errorCode) {
            400 -> {
                ex = ResponseException(ERROR.HTTP_BAD_REQUEST)
            }

            401 -> {
                ex = ResponseException(ERROR.HTTP_UNAUTHORIZED)
            }

            403 -> {
                ex = ResponseException(ERROR.HTTP_FORBIDDEN)
            }

            404 -> {
                ex = ResponseException(ERROR.HTTP_NOT_FOUND)
            }

            408 -> {
                ex = ResponseException(ERROR.HTTP_REQUEST_TIMEOUT)
            }

            500 -> {
                ex = ResponseException(ERROR.HTTP_INTERNAL_SERVER_ERROR)
            }

            502 -> {
                ex = ResponseException(ERROR.HTTP_BAD_GATEWAY)
            }

            503 -> {
                ex = ResponseException(ERROR.HTTP_SERVICE_UNAVAILABLE)
            }

            504 -> {
                ex = ResponseException(ERROR.HTTP_GATEWAY_TIMEOUT)
            }

            else -> {
                ex = ResponseException(response.code, response.message)
            }
        }
        callErrorHandler { handleError(ex) }
        callErrorHandler { handleResponseError(ex) }
        return ex
    }

    /**
     * 转换错误处理错误
     */
    @JvmStatic
    fun handleConversionException(e: Throwable): ConversionException {
        val ex = ConversionException(ERROR.PARSE_ERROR, e)
        callErrorHandler { handleError(ex) }
        callErrorHandler { handleConversionError(ex) }
        return ex
    }

    /**
     * api服务错误处理错误
     */
    @JvmStatic
    fun handleApiServiceError(e: ApiException): ApiException {
        callErrorHandler { handleError(e) }
        callErrorHandler { handleApiServiceError(e) }
        return e
    }


    private fun callErrorHandler(block: IErrorHandler.() -> Unit) {
        EasyRetrofit.errorHandlers.forEach {
            it.block()
        }
    }
}