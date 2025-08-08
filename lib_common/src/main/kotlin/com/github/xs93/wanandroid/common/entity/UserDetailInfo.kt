package com.github.xs93.wanandroid.common.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


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
@Parcelize
data class UserDetailInfo(
    @param:Json(name = "coinInfo")
    val coinInfo: UserCoinInfo = UserCoinInfo(),
    @param:Json(name = "collectArticleInfo")
    val collectArticleInfo: UserCollectArticleInfo = UserCollectArticleInfo(),
    @param:Json(name = "userInfo")
    val userInfo: User = User()
) : Parcelable