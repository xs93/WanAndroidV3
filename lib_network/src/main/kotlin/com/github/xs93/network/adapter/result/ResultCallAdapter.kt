package com.github.xs93.network.adapter.result

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/7 9:35
 * @email 466911254@qq.com
 */
internal class ResultCallAdapter(private val successType: Type) :
    CallAdapter<Any, Call<Result<*>>> {
    override fun responseType(): Type {
        return successType
    }

    @Suppress("UNCHECKED_CAST")
    override fun adapt(call: Call<Any>): Call<Result<*>> {
        return ResultCall(call) as Call<Result<*>>
    }
}

