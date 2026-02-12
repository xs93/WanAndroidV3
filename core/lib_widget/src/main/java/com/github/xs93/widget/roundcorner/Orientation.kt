package com.github.xs93.widget.roundcorner

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/11/21 16:58
 * @description 渐变颜色方向
 *
 */
enum class Orientation(val value: Int) {
    LEFT_RIGHT(0),
    TL_BR(1),
    TOP_BOTTOM(2),
    TR_BL(3),
    RIGHT_LEFT(4),
    BR_TL(5),
    BOTTOM_TOP(6),
    BL_TR(7)
}