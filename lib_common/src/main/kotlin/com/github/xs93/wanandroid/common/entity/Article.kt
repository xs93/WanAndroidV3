package com.github.xs93.wanandroid.common.entity


import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * WanAndroid 文章对象信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/1 13:38
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class Article(
    @Json(name = "adminAdd")
    val adminAdd: Boolean,
    @Json(name = "apkLink")
    val apkLink: String,
    @Json(name = "audit")
    val audit: Int,
    @Json(name = "author")
    val author: String,
    @Json(name = "canEdit")
    val canEdit: Boolean,
    @Json(name = "chapterId")
    val chapterId: Int,
    @Json(name = "chapterName")
    val chapterName: String,
    @Json(name = "collect")
    val collect: Boolean,
    @Json(name = "courseId")
    val courseId: Int,
    @Json(name = "desc")
    val desc: String,
    @Json(name = "descMd")
    val descMd: String,
    @Json(name = "envelopePic")
    val envelopePic: String,
    @Json(name = "fresh")
    val fresh: Boolean,
    @Json(name = "host")
    val host: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "isAdminAdd")
    val isAdminAdd: Boolean,
    @Json(name = "link")
    val link: String,
    @Json(name = "niceDate")
    val niceDate: String,
    @Json(name = "niceShareDate")
    val niceShareDate: String,
    @Json(name = "origin")
    val origin: String,
    @Json(name = "prefix")
    val prefix: String,
    @Json(name = "projectLink")
    val projectLink: String,
    @Json(name = "publishTime")
    val publishTime: Long,
    @Json(name = "realSuperChapterId")
    val realSuperChapterId: Int,
    @Json(name = "selfVisible")
    val selfVisible: Int,
    @Json(name = "shareDate")
    val shareDate: Long?,
    @Json(name = "shareUser")
    val shareUser: String,
    @Json(name = "superChapterId")
    val superChapterId: Int,
    @Json(name = "superChapterName")
    val superChapterName: String,
    @Json(name = "tags")
    val tags: List<Tag>,
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: Int,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "visible")
    val visible: Int,
    @Json(name = "zan")
    val zan: Int
) : Parcelable {


    val showAuthorName: String
        get() = author.ifEmpty { shareUser }

    val isTop: Boolean
        get() = type == 1
}