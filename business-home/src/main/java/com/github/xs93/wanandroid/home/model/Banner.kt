package com.github.xs93.wanandroid.home.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


/**
 * Banner数据对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 14:12
 * @email 466911254@qq.com
 */
@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class Banner(
    val desc: String = "", // 我们支持订阅啦~
    val id: Int = 0, // 30
    val imagePath: String = "", // https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png
    val isVisible: Int = 0, // 1
    val order: Int = 0, // 2
    val title: String = "", // 我们支持订阅啦~
    val type: Int = 0, // 0
    val url: String = "" // https://www.wanandroid.com/blog/show/3352
) : Parcelable