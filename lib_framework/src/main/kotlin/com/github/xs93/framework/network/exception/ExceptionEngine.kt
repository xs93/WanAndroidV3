package com.github.xs93.framework.network.exception

import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

/**
 *
 * 错误处理引擎
 *
 * @author xushuai
 * @date   2022/9/2-15:00
 * @email  466911254@qq.com
 */
object ExceptionEngine {

    //对应HTTP的状态码
    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504
    private const val TOKEN_FAILED = 409

    @JvmStatic
    fun handleException(e: Throwable): ResponseException {
        val ex: ResponseException
        //HTTP错误
        when (e) {
            is HttpException -> {
                //均视为网络错误
                ex = when (e.code()) {
                    UNAUTHORIZED -> ResponseException(e.code(), "未验证", e)
                    FORBIDDEN -> ResponseException(e.code(), "服务器禁止访问", e)
                    NOT_FOUND -> ResponseException(e.code(), "服务器不存在", e)
                    REQUEST_TIMEOUT -> ResponseException(e.code(), "请求超时", e)
                    GATEWAY_TIMEOUT -> ResponseException(e.code(), "网关超时", e)
                    BAD_GATEWAY -> ResponseException(e.code(), "网关错误", e)
                    INTERNAL_SERVER_ERROR -> ResponseException(e.code(), "服务器内部错误", e)
                    SERVICE_UNAVAILABLE -> ResponseException(e.code(), "服务器不可用", e)
                    TOKEN_FAILED -> ResponseException(e.code(), "请求冲突", e)
                    else -> ResponseException(e.code(), "请求错误", e)
                }
            }
            is JSONException, is ParseException -> {
                ex = ResponseException(ErrorCode.PARSE_ERROR, "解析数据错误", e)
            }
            is ConnectException, is SocketTimeoutException, is UnknownHostException -> {
                ex = ResponseException(ErrorCode.NETWORK_ERROR, "网络连接错误", e)
            }
            is SSLHandshakeException -> {
                ex = ResponseException(ErrorCode.SSL_ERROR, "ssl证书验证失败", e)
            }
            is ApiException -> {
                ex = ResponseException(e.errorCode, e.errorMessage, e)
            }
            else -> {
                //未知错误
                ex = ResponseException(ErrorCode.UNKNOWN, "未知错误", e)
            }
        }
        return ex
    }
}