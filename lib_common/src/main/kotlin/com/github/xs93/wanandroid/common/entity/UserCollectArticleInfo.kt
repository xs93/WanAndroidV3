package com.github.xs93.wanandroid.common.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * 用户收藏文章信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/3 16:23
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
data class UserCollectArticleInfo(
    @Json(name = "count")
    val count: Int
)