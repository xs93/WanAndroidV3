package com.github.xs93.network.exception

import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
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

    var safeRequestApiErrorHandler: ((Throwable) -> Unit)? = null

    @JvmStatic
    fun handleException(e: Throwable): ApiException {
        val ex: ApiException
        //HTTP错误
        when (e) {
            is ApiException -> {
                ex = e
            }

            is NoNetworkException -> {
                ex = ApiException(ERROR.NETWORK_ERROR, e)
            }

            is HttpException -> {
                //均视为网络错误
                ex = when (e.code()) {
                    ERROR.UNAUTHORIZED.code -> ApiException(ERROR.UNAUTHORIZED, e)
                    ERROR.FORBIDDEN.code -> ApiException(ERROR.FORBIDDEN, e)
                    ERROR.NOT_FOUND.code -> ApiException(ERROR.NOT_FOUND, e)
                    ERROR.REQUEST_TIMEOUT.code -> ApiException(ERROR.REQUEST_TIMEOUT, e)
                    ERROR.GATEWAY_TIMEOUT.code -> ApiException(ERROR.GATEWAY_TIMEOUT, e)
                    ERROR.INTERNAL_SERVER_ERROR.code -> ApiException(ERROR.INTERNAL_SERVER_ERROR, e)
                    ERROR.BAD_GATEWAY.code -> ApiException(ERROR.BAD_GATEWAY, e)
                    ERROR.SERVICE_UNAVAILABLE.code -> ApiException(ERROR.SERVICE_UNAVAILABLE, e)
                    else -> ApiException(e.code(), e.message(), e)
                }
            }

            is JSONException,
            is ParseException -> {
                ex = ApiException(ERROR.PARSE_ERROR, e)
            }

            is ConnectException -> {
                ex = ApiException(ERROR.NETWORK_ERROR, e)
            }

            is SSLException -> {
                ex = ApiException(ERROR.SSL_ERROR, e)
            }

            is SocketException,
            is SocketTimeoutException -> {
                ex = ApiException(ERROR.TIMEOUT_ERROR, e)
            }

            is UnknownHostException -> {
                ex = ApiException(ERROR.UNKNOWN_HOST, e)
            }

            else -> {
                ex = if (!e.message.isNullOrBlank()) {
                    ApiException(ERROR.UNKNOWN.code, e.message!!, e)
                } else {
                    ApiException(ERROR.UNKNOWN, e)
                }
            }
        }
        return ex
    }
}