package com.github.xs93.wan.common.entity

import android.os.Parcelable
import androidx.annotation.Keep
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
    val articleList: List<Article>,
    val author: String,
    val children: List<ProjectTree>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Parcelable