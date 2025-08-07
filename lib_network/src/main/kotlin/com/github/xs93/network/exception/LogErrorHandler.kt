package com.github.xs93.network.exception

import android.util.Log

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/7 14:31
 * @description 默认错误处理,打印错误处理
 *
 */
class LogErrorHandler : IErrorHandler {
    override fun handleError(e: Throwable) {

    }

    override fun handleNetworkError(e: NetworkException) {
        Log.e("LogErrorHandler", "网络异常, 请检查网络", e)
    }

    override fun handleResponseError(e: ResponseException) {
        Log.e("LogErrorHandler", "服务器请求异常,请检查服务器", e)
    }

    override fun handleConversionError(e: ConversionException) {
        Log.e("LogErrorHandler", "数据解析异常,请检查数据解析", e)
    }

    override fun checkApiServiceError(any: Any?) {
        Log.i("LogErrorHandler", "checkApiServiceError: $any")
    }

    override fun handleApiServiceError(e: ApiException) {
        Log.e("LogErrorHandler", "接口请求业务逻辑错误,请检查业务逻辑", e)
    }
}