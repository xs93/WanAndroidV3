package com.github.xs93.framework.network.model

/**
 *
 * 反应请求流程状态对象
 *
 * @author xushuai
 * @date   2022/9/2-14:37
 * @email  466911254@qq.com
 */
sealed class RequestState<out T : Any> {

    object Loading : RequestState<Nothing>()

    data class Success<out T : Any>(val data: T? = null) : RequestState<T>()

    data class Error(val exception: Exception) : RequestState<Nothing>()

    override fun toString(): String {

        return when (this) {
            is Loading -> "Loading"
            is Success -> "Success[data = $data]"
            is Error -> "Error[ throwable = $exception]"
        }
    }
}