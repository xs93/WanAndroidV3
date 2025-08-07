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
class ApiException : Exception {
    var errorCode: Int
        private set
    var errorMsg: String
        private set

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

class NetworkException(error: ERROR, throwable: Throwable? = null) : IOException(throwable) {

    val errorCode: Int = error.code
    val errorMsg: String = error.errMsg

    override fun toString(): String {
        return "errorCode = $errorCode,errorMsg = $errorMsg,$cause"
    }
}

class ResponseException : Exception {

    var errorCode: Int
        private set
    var errorMsg: String
        private set

    constructor(error: ERROR, throwable: Throwable? = null) : super(throwable) {
        errorCode = error.code
        errorMsg = error.errMsg
    }

    constructor(code: Int, msg: String, throwable: Throwable? = null) : super(throwable) {
        errorCode = code
        errorMsg = msg
    }

    override fun toString(): String {
        return "errorCode = $errorCode,errorMsg = $errorMsg,$cause"
    }
}

class ConversionException : Exception {
    var errorCode: Int
        private set
    var errorMsg: String
        private set

    constructor(error: ERROR, throwable: Throwable? = null) : super(throwable) {
        errorCode = error.code
        errorMsg = error.errMsg
    }
}