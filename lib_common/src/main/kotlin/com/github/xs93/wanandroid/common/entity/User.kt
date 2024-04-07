package com.github.xs93.wanandroid.common.entity

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
 * @date 2024/4/3 16:20
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class User(
    @Json(name = "admin")
    val admin: Boolean = false,
    @Json(name = "chapterTops")
    val chapterTops: List<String> = listOf(),
    @Json(name = "coinCount")
    val coinCount: Int = 0,
    @Json(name = "collectIds")
    val collectIds: List<Int> = listOf(),
    @Json(name = "email")
    val email: String = "",
    @Json(name = "icon")
    val icon: String = "",
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "nickname")
    val nickname: String = "",
    @Json(name = "password")
    val password: String = "",
    @Json(name = "publicName")
    val publicName: String = "",
    @Json(name = "token")
    val token: String = "",
    @Json(name = "type")
    val type: Int = 0,
    @Json(name = "username")
    val username: String = "",
) : Parcelable
