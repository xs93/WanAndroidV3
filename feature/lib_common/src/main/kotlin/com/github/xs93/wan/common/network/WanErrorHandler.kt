package com.github.xs93.wan.common.network

import com.github.xs93.core.toast.ToastUtils
import com.github.xs93.network.exception.ApiException
import com.github.xs93.network.exception.ConversionException
import com.github.xs93.network.exception.IErrorHandler
import com.github.xs93.network.exception.NetworkException
import com.github.xs93.network.exception.ResponseException
import com.github.xs93.wan.common.R
import com.github.xs93.wan.model.model.WanResponse
import com.github.xs93.wan.router.RouterHelper

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/7 14:40
 * @description 自定义全局网络请求错误处理
 *
 */
class WanErrorHandler : IErrorHandler {
    override fun handleError(e: Throwable) {

    }

    override fun handleNetworkError(e: NetworkException) {
        ToastUtils.showToast(R.string.network_error)
    }

    override fun handleResponseError(e: ResponseException) {
        ToastUtils.showToast(R.string.server_error)
    }

    override fun handleConversionError(e: ConversionException) {
        ToastUtils.showToast(R.string.parse_error)
    }

    override fun checkApiServiceError(any: Any?) {
        if (any is WanResponse<*>) {
            when {
                any.isNotLogin() -> {
                    RouterHelper.toLogin()
                }
            }
            if (any.isFailed()) {
                throw ApiException(any.errorCode, any.errorMessage)
            }
        }
    }

    override fun handleApiServiceError(e: ApiException) {
        ToastUtils.showToast(e.errorMsg)
    }
}