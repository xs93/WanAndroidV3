package com.github.xs93.wan.common.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

/**
 * tab数据对象
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 14:55
 * @email 466911254@qq.com
 */
@Parcelize
data class SimpleTabBean(
    @param:StringRes val titleResId: Int? = null,
    @param:DrawableRes val iconResId: Int? = null
) : Parcelable
