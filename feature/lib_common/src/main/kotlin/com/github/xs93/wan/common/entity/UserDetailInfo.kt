package com.github.xs93.wan.common.entity

import android.os.Parcelable
import androidx.annotation.Keep
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
    val coinInfo: UserCoinInfo = UserCoinInfo(),
    val collectArticleInfo: UserCollectArticleInfo = UserCollectArticleInfo(),
    val userInfo: User = User()
) : Parcelable