package com.github.xs93.wanandroid.common.model

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
 * @date 2022/9/28 11:55
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class CoinInfo(
    @Json(name = "coinCount")
    var coinCount: Int? = null,
    @Json(name = "level")
    var level: Int? = null,
    @Json(name = "nickname")
    var nickname: String? = null,
    @Json(name = "rank")
    var rank: String? = null,
    @Json(name = "userId")
    var userId: Int? = null,
    @Json(name = "username")
    var username: String? = null
) : Parcelable