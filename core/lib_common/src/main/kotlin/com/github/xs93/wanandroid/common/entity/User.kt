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
    @param:Json(name = "admin")
    val admin: Boolean = false,
    @param:Json(name = "chapterTops")
    val chapterTops: List<String> = listOf(),
    @param:Json(name = "coinCount")
    val coinCount: Int = 0,
    @param:Json(name = "collectIds")
    val collectIds: List<Int> = listOf(),
    @param:Json(name = "email")
    val email: String = "",
    @param:Json(name = "icon")
    val icon: String = "",
    @param:Json(name = "id")
    val id: Int = 0,
    @param:Json(name = "nickname")
    val nickname: String = "",
    @param:Json(name = "password")
    val password: String = "",
    @param:Json(name = "publicName")
    val publicName: String = "",
    @param:Json(name = "token")
    val token: String = "",
    @param:Json(name = "type")
    val type: Int = 0,
    @param:Json(name = "username")
    val username: String = "",
) : Parcelable
