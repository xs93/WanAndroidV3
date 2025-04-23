package com.github.xs93.demo.entity

import java.io.Serializable

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/22 11:42
 * @description
 *
 */
data class TestResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?
) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }
}
