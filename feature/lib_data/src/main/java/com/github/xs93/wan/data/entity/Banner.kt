package com.github.xs93.wan.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
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
    val id: Long,
    val title: String,
    val imagePath: String,
    val desc: String,
    val isVisible: Int,
    val order: Int,
    val type: Int,
    val url: String
) : Parcelable