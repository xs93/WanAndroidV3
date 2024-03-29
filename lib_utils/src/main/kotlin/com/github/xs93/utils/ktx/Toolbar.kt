package com.github.xs93.utils.ktx

import android.view.View
import android.widget.Toolbar
import com.github.xs93.utils.listener.SingleClickListener

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/3/28 9:49
 * @email 466911254@qq.com
 */

fun Toolbar.setSingleNavigationClickListener(
    singleClickInterval: Long? = 800,
    listener: View.OnClickListener? = null
) {
    val interval = singleClickInterval ?: 800
    setNavigationOnClickListener(SingleClickListener(interval, listener))
}