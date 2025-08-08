package com.github.xs93.network.adapter.result

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 返回kotlin Result 對象的Call Adapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/7 10:05
 * @email 466911254@qq.com
 */
class ResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? { // suspend 函数在 Retrofit 中的返回值其实是 `Call`
        if (Call::class.java != getRawType(returnType)) return null // 检查返回类型是否为 `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }
        val upperBound = getParameterUpperBound(0, returnType)
        if (upperBound is ParameterizedType && upperBound.rawType == Result::class.java) {
            return ResultCallAdapter(getParameterUpperBound(0, upperBound))
        }
        return null
    }
}