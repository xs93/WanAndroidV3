package com.github.xs93.widget.roundcorner

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/10/31 16:46
 * @description
 *
 */
interface IRoundCorner {

    val roundCornerHelper: RoundCornerHelper

    /**
     * 设置圆角
     * @param radiusDp 圆角半径
     */
    fun setRadius(radiusDp: Float) = roundCornerHelper.setRadius(radiusDp)

    /**
     * 设置圆角
     * @param topStartRadiusDp 顶部左圆角半径
     * @param topEndRadiusDp 顶部右圆角半径
     * @param bottomStartRadiusDp 底部左圆角半径
     * @param bottomEndRadiusDp 底部右圆角半径
     */
    fun setRadius(
        topStartRadiusDp: Float,
        topEndRadiusDp: Float,
        bottomStartRadiusDp: Float,
        bottomEndRadiusDp: Float
    ) = roundCornerHelper.setRadius(topStartRadiusDp, topEndRadiusDp, bottomStartRadiusDp, bottomEndRadiusDp)

    /**
     * 设置边框宽度
     * @param strokeWidth 边框宽度
     */
    fun setStrokeWidth(strokeWidth: Float) = roundCornerHelper.setStrokeWidth(strokeWidth)

    /**
     * 边框颜色
     * @param strokeColor 边框颜色
     */
    fun setStrokeColor(strokeColor: Int) = roundCornerHelper.setStrokeColor(strokeColor)

    /**
     * 设置边框宽度和颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     */
    fun setStrokeWidthAndColor(strokeWidth: Float, strokeColor: Int) =
        roundCornerHelper.setStrokeWidthAndColor(strokeWidth, strokeColor)

    /**
     * 渐变边框颜色
     * @param colors 边框颜色
     * @param positions 颜色位置
     */
    fun setStrokeColors(colors: IntArray, positions: FloatArray? = null) =
        roundCornerHelper.setStrokeColors(colors, positions)

    /**
     * 渐变边框颜色方向
     * @param orientation 渐变方向
     */
    fun setStrokeOrientation(orientation: Orientation) =
        roundCornerHelper.setStrokeOrientation(orientation)

    /**
     * 背景颜色
     * @param bgColor 背景颜色
     */
    fun setBgColor(bgColor: Int) = roundCornerHelper.setBgColor(bgColor)

    /**
     * 渐变背景颜色
     * @param colors 背景颜色
     * @param positions 颜色位置
     */
    fun setBgColors(colors: IntArray, positions: FloatArray? = null) =
        roundCornerHelper.setBgColors(colors, positions)

    /**
     * 渐变背景颜色方向
     * @param orientation 渐变方向
     */
    fun setBgOrientation(orientation: Orientation) =
        roundCornerHelper.setBgOrientation(orientation)
}
