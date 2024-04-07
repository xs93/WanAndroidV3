package com.github.xs93.wanandroid.common.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * 用户详细信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/4/3 16:24
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
data class UserDetailInfo(
    @Json(name = "coinInfo")
    val coinInfo: UserCoinInfo,
    @Json(name = "collectArticleInfo")
    val collectArticleInfo: UserCollectArticleInfo,
    @Json(name = "userInfo")
    val userInfo: User
)