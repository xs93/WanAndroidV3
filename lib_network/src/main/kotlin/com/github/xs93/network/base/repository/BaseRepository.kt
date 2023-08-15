package com.github.xs93.network.base.repository


import com.github.xs93.network.exception.ERROR
import com.github.xs93.network.exception.ServiceApiException
import com.github.xs93.network.model.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * 基础数据仓库类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/5/17 17:34
 */
open class BaseRepository {

    protected suspend fun <T> requestApi(timeOut: Long = 10 * 1000L, block: suspend () -> T?): T? {
        val resp = withContext(Dispatchers.IO) {
            withTimeout(timeOut) {
                block()
            }
        }

        if (resp is ApiResponse<*>) {
            if (resp.isNotLogin()) {
                throw ServiceApiException(ERROR.NOT_LOGIN, null)
            }
            if (resp.isFailed()) {
                throw ServiceApiException(resp.errorCode, resp.errorMessage, null)
            }
        }
        return resp
    }
}