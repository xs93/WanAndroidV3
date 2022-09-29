package com.github.xs93.wanandroid.common.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/9/28 11:58
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class UserInfo(
    @Json(name = "coinInfo")
    var coinInfo: CoinInfo? = CoinInfo(),
    @Json(name = "userInfo")
    var userInfo: AccountInfo? = AccountInfo()
) : Parcelable