package com.github.xs93.core.ktx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/1/25 15:31
 * @email 466911254@qq.com
 */

fun ViewGroup.inflate(layoutResId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutResId, this, attachToRoot)
}