package com.github.xs93.wanandroid.common.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


/**
 * 首页banner信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 9:44
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class Banner(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "imagePath")
    val imagePath: String,
    @Json(name = "desc")
    val desc: String,
    @Json(name = "isVisible")
    val isVisible: Int,
    @Json(name = "order")
    val order: Int,
    @Json(name = "type")
    val type: Int,
    @Json(name = "url")
    val url: String
) : Parcelable