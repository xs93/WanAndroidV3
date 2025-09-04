package com.github.xs93.wan.common.entity

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/9/4 9:54
 * @description 首页Tab对象信息
 *
 */
@Parcelize
data class HomeTabBean(@param:StringRes val titleResId: Int) : Parcelable
