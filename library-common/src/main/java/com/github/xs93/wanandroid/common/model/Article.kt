package com.github.xs93.wanandroid.common.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


/**
 * WanAndroid文章对象信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/11/1 13:36
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class Article(
    val adminAdd: Boolean = false, // false
    val apkLink: String = "",
    val audit: Int = 0, // 1
    val author: String = "", // 郭霖
    val canEdit: Boolean = false, // false
    val chapterId: Int = 0, // 409
    val chapterName: String = "", // 郭霖
    val collect: Boolean = false, // false
    val courseId: Int = 0, // 13
    val desc: String = "",
    val descMd: String = "",
    val envelopePic: String = "",
    val fresh: Boolean = false, // false
    val host: String = "",
    val id: Int = 0, // 24804
    val isAdminAdd: Boolean = false, // false
    val link: String = "", // https://mp.weixin.qq.com/s/vI435S_2ICuHBj3NOHhfaw
    val niceDate: String = "", // 2022-10-28 00:00
    val niceShareDate: String = "", // 1天前
    val origin: String = "",
    val prefix: String = "",
    val projectLink: String = "",
    val publishTime: Long = 0, // 1666886400000
    val realSuperChapterId: Int = 0, // 407
    val selfVisible: Int = 0, // 0
    val shareDate: Long = 0, // 1667143083000
    val shareUser: String = "",
    val superChapterId: Int = 0, // 408
    val superChapterName: String = "", // 公众号
    val tags: List<Tag> = listOf(),
    val title: String = "", // Drawable+Animator，将优雅进行到底
    val type: Int = 0, // 0
    val userId: Int = 0, // -1
    val visible: Int = 0, // 1
    val zan: Int = 0 // 0
) : Parcelable

@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class Tag(
    val name: String = "", // 公众号
    val url: String = "" // /wxarticle/list/409/1
) : Parcelable