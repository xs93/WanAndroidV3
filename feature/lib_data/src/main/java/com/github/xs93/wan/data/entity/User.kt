package com.github.xs93.wan.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
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
    val admin: Boolean = false,
    val chapterTops: List<String> = listOf(),
    val coinCount: Int = 0,
    val collectIds: List<Int> = listOf(),
    val email: String = "",
    val icon: String = "",
    val id: Int = 0,
    val nickname: String = "",
    val password: String = "",
    val publicName: String = "",
    val token: String = "",
    val type: Int = 0,
    val username: String = "",
) : Parcelable