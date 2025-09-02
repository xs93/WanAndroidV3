package com.github.xs93.network.model

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/27 14:10
 * @description 请求地址配置, 用于动态切换请求地址，
 *
 * 获取方法和类的 [com.github.xs93.network.annotation.DynamicBaseUrl] 注解的参数和 @Url 注解的参数的位置
 *
 */
data class UrlsConfig(
    val methodUrl: String?,
    val methodUrlKey: String?,
    val clazzUrl: String?,
    val clazzUrlKey: String?,
    val urlAnnotationIndex: Int,
)
