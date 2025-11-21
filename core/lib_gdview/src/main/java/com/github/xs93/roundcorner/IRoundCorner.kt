package com.github.xs93.roundcorner

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/10/31 16:46
 * @description
 *
 */
interface IRoundCorner {


    fun setRadius(radiusDp: Float)

    fun setRadius(
        topStartRadiusDp: Float,
        topEndRadiusDp: Float,
        bottomStartRadiusDp: Float,
        bottomEndRadiusDp: Float
    )

    fun setStrokeWidth(strokeWidth: Float)

    fun setStrokeColor(strokeColor: Int)

    fun setStrokeWidthAndColor(strokeWidth: Float, strokeColor: Int)

    fun setStrokeColors(colors: IntArray, positions: FloatArray? = null)

    fun setStrokeOrientation(orientation: Orientation)
}