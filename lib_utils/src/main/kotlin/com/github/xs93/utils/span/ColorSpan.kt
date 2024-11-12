package com.github.xs93.utils.span

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import androidx.annotation.ColorRes

/**
 * 快速渲染文本颜色
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/12 10:33
 * @email 466911254@qq.com
 */
class ColorSpan(color: Int) : ForegroundColorSpan(color) {
    constructor(color: String) : this(Color.parseColor(color))
    constructor(context: Context, @ColorRes colorRes: Int) : this(context.getColor(colorRes))
}