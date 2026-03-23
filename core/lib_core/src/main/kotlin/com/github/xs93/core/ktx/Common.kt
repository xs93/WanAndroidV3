package com.github.xs93.core.ktx

import kotlin.coroutines.cancellation.CancellationException

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/3/4 17:51
 * @description
 *
 */

/**
 * 运行suspend方法，并返回结果,需要抛出CancellationException异常,不拦截，否则无法取消协程
 */
inline fun <T> runSuspendCatching(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}