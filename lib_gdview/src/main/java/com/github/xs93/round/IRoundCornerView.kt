package com.github.xs93.round

import android.graphics.Canvas

/**
 * 实现ViewGroup 裁剪圆角策略
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/14 10:34
 * @email 466911254@qq.com
 */
interface IRoundCornerView {

    fun beforeDispatchDraw(canvas: Canvas?)

    fun afterDispatchDraw(canvas: Canvas?)
}