package com.github.xs93.wan.common.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * 导航
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 16:56
 * @email 466911254@qq.com
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class Navigation(
    val articles: List<Article>,
    val cid: Int,
    val name: String
) : Parcelable
