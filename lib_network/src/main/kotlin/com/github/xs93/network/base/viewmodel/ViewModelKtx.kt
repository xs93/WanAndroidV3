package com.github.xs93.network.base.viewmodel

import androidx.lifecycle.ViewModel
import com.github.xs93.network.exception.ExceptionHandler

/**
 * ViewModel 网络扩展函数
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/15 13:40
 * @email 466911254@qq.com
 */

/**
 * 安全请求接口方法
 * @param T Any? 接口返回数据泛型类型
 * @receiver ViewModel
 * @param errorBlock ((Throwable) -> Unit)? 错误处理,默认交由全局配置处理
 * @param block suspend () -> T? 接口请求方法
 * @return T? 接口返回对象
 */
suspend fun <T> ViewModel.safeRequestApi(
    errorBlock: ((Throwable) -> Unit)? = ExceptionHandler.safeRequestApiErrorHandler,
    block: suspend () -> T?
): T? {
    return try {
        block()
    } catch (e: Throwable) {
        val ex = ExceptionHandler.handleException(e)
        errorBlock?.invoke(ex)
        null
    }
}
