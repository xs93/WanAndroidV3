package com.github.xs93.wanandroid.common.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


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
@Parcelize
data class UserCoinInfo(
    @Json(name = "coinCount")
    val coinCount: Int = 0,
    @Json(name = "level")
    val level: Int = 0,
    @Json(name = "nickname")
    val nickname: String = "",
    @Json(name = "rank")
    val rank: String = "",
    @Json(name = "userId")
    val userId: Int = 0,
    @Json(name = "username")
    val username: String = ""
) : Parcelable