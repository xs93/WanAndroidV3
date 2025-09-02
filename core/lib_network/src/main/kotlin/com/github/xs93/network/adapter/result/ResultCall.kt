package com.github.xs93.network.adapter.result

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

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
                val result: Result<T>
                if (response.isSuccessful) {
                    val body = response.body()
                    result = if (body == null) {
                        Result.failure(NullPointerException("response body is null"))
                    } else {
                        Result.success(body)
                    }
                } else {
                    result = Result.failure(HttpException(response))
                }
                callback.onResponse(
                    this@ResultCall, Response.success(response.code(), result)
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(this@ResultCall, Response.success(Result.failure(t)))
            }
        })
    }

    override fun execute(): Response<Result<T>> {
        val result = try {
            val executeResponse = delegate.execute()
            if (executeResponse.isSuccessful) {
                val body = executeResponse.body()
                if (body == null) {
                    Result.failure(NullPointerException("response body is null"))
                } else {
                    Result.success(body)
                }
            } else {
                Result.failure(HttpException(executeResponse))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
        return Response.success(result)
    }

    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())

    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}