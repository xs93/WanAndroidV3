package com.github.xs93.network.exception

import java.io.IOException

/**
 *
 * 接口请求错误
 *
 * @author xushuai
 * @date   2022/9/2-14:33
 * @email  466911254@qq.com
 */
open class ApiException : Exception {

    var errorCode: Int
    var errorMsg: String

    @JvmOverloads
    constructor(error: ERROR, throwable: Throwable? = null) : super(throwable) {
        errorCode = error.code
        errorMsg = error.errMsg
    }

    @JvmOverloads
    constructor(code: Int, msg: String, throwable: Throwable? = null) : super(throwable) {
        errorCode = code
        errorMsg = msg
    }

    override fun toString(): String {
        return "errorCode = $errorCode,errorMsg = $errorMsg,$cause"
    }
}

class ServiceApiException : ApiException {
    constructor(error: ERROR, throwable: Throwable?) : super(error, throwable)
    constructor(code: Int, msg: String, throwable: Throwable?) : super(code, msg, throwable)
}

class NoNetworkException(error: ERROR, throwable: Throwable? = null) : IOException(throwable) {

    var errorCode: Int
    var errorMsg: String

    init {
        errorCode = error.code
        errorMsg = error.errMsg
    }
}