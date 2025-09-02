package com.github.xs93.network.moshi.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/7/28
 * @description 添加注解，只有添加注解的的字段才使用此adapter
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class BooleanType

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/7/28
 * @description 将Int 和Boolean 互转的适配器
 *
 */
class BooleanAdapter {

    @FromJson
    @BooleanType
    fun formJson(value: Int?): Boolean {
        return value != null && value >= 1
    }

    @ToJson
    fun toJson(@BooleanType value: Boolean): Int {
        return if (value) 1 else 0
    }
}