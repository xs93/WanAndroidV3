package com.github.xs93.wanandroid.common.entity


import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 *  WanAndroid 文章tag
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/1 13:38
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class Tag(
    @Json(name = "name")
    val name: String,
    @Json(name = "url")
    val url: String
) : Parcelable