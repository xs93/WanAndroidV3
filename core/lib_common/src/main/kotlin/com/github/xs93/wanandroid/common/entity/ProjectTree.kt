package com.github.xs93.wanandroid.common.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


/**
 * 项目分类对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/12 9:11
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class ProjectTree(
    @Json(name = "articleList")
    val articleList: List<Article>,
    @Json(name = "author")
    val author: String,
    @Json(name = "children")
    val children: List<ProjectTree>,
    @Json(name = "courseId")
    val courseId: Int,
    @Json(name = "cover")
    val cover: String,
    @Json(name = "desc")
    val desc: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "lisense")
    val lisense: String,
    @Json(name = "lisenseLink")
    val lisenseLink: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "order")
    val order: Int,
    @Json(name = "parentChapterId")
    val parentChapterId: Int,
    @Json(name = "type")
    val type: Int,
    @Json(name = "userControlSetTop")
    val userControlSetTop: Boolean,
    @Json(name = "visible")
    val visible: Int
) : Parcelable