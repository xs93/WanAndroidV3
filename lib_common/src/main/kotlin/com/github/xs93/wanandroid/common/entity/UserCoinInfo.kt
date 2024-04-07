package com.github.xs93.wanandroid.common.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * 用户积分信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/3 16:22
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
data class UserCoinInfo(
    @Json(name = "coinCount")
    val coinCount: Int,
    @Json(name = "level")
    val level: Int,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "rank")
    val rank: String,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "username")
    val username: String
)