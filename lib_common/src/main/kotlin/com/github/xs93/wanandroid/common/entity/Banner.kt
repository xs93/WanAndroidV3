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
    @param:Json(name = "id")
    val id: Long,
    @param:Json(name = "title")
    val title: String,
    @param:Json(name = "imagePath")
    val imagePath: String,
    @param:Json(name = "desc")
    val desc: String,
    @param:Json(name = "isVisible")
    val isVisible: Int,
    @param:Json(name = "order")
    val order: Int,
    @param:Json(name = "type")
    val type: Int,
    @param:Json(name = "url")
    val url: String
) : Parcelable