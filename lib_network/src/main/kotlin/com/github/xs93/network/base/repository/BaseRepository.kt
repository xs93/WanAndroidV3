package com.github.xs93.network.base.repository


import com.github.xs93.network.exception.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * 基础数据仓库类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/5/17 17:34
 */
open class BaseRepository {

    protected suspend fun <T> safeRequestApi(
        errorHandler: ((throwable: Throwable) -> Unit)? = null,
        block: suspend () -> T?
    ): T? {
        val resp = withContext(Dispatchers.IO) {
            runCatching {
                block()
            }.onFailure {
                it.printStackTrace()
                val apiException = ExceptionHandler.handleException(it)
                if (errorHandler != null) {
                    errorHandler.invoke(apiException)
                } else {
                    ExceptionHandler.safeRequestApiErrorHandler?.invoke(apiException)
                }
            }.getOrNull()
        }
        return resp
    }

    protected inline fun <T> runSafeSuspendCatching(block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val apiException = ExceptionHandler.handleException(e)
            Result.failure(apiException)
        }
    }
}