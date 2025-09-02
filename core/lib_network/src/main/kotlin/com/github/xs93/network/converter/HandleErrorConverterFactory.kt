package com.github.xs93.network.converter

import android.util.Log
import com.github.xs93.network.EasyRetrofit
import com.github.xs93.network.exception.ApiException
import com.github.xs93.network.exception.ExceptionHandler
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/7 13:23
 * @description 错误处理转换器，包括数据解析错误和服务器业务code错误
 *
 */
class HandleErrorConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create(): Converter.Factory {
            return HandleErrorConverterFactory()
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation?>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        Log.d("AAA", "responseBodyConverter")
        val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter<ResponseBody, Any> { value ->
            val result = try {
                delegate.convert(value)
            } catch (e: Exception) {
                throw ExceptionHandler.handleConversionException(e)
            }
            try {
                EasyRetrofit.errorHandlers.forEach {
                    it.checkApiServiceError(any = result)
                }
            } catch (e: ApiException) {
                throw ExceptionHandler.handleApiServiceError(e)
            }
            return@Converter result
        }
    }
}
