package com.github.xs93.wan.common.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


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
@Parcelize
data class UserCollectArticleInfo(
    val count: Int = 0
) : Parcelable