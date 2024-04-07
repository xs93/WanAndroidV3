package com.github.xs93.network.adapter.result

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

/**
 * Call转为ResultCall
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/7 9:49
 * @email 466911254@qq.com
 */
class ResultCall<T>(private val delegate: Call<T>) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    callback.onResponse(
                        this@ResultCall, Response.success(response.code(), Result.success(response.body()!!))
                    )
                } else {
                    callback.onResponse(this@ResultCall, Response.success(Result.failure(HttpException(response))))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (call.isCanceled) { // 忽略请求被canceled的情况
                    return
                }
                callback.onResponse(this@ResultCall, Response.success(Result.failure(t)))
            }
        })
    }

    override fun execute(): Response<Result<T>> {
        return Response.success(Result.success(delegate.execute().body()!!))
    }

    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())

    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}