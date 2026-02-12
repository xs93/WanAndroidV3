package com.github.xs93.wan.common.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.xs93.core.ktx.getColorByAttr

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/8/6 16:33
 * @description 自动使用项目主题色的SwipeRefreshLayout
 *
 */
class SchemeColorSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    init {
        setColorSchemeColors(context.getColorByAttr(androidx.appcompat.R.attr.colorPrimary))
    }
}