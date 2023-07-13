package com.github.xs93.wanandroid.common.network

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass


/**
 * 分页数据模型
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/1 13:38
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
class PageResp<out T>(
    val curPage: Int = 0, // 2
    val offset: Int = 0, // 20
    val datas: List<T> = listOf(),
    val over: Boolean = false, // false
    val pageCount: Int = 0, // 667
    val size: Int = 0, // 20
    val total: Int = 0 // 13321
)