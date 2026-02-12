package com.github.xs93.widget.roundcorner.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.xs93.widget.roundcorner.IRoundCorner
import com.github.xs93.widget.roundcorner.Orientation
import com.github.xs93.widget.roundcorner.RoundCornerHelper

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/10/31 14:52
 * @description 圆角ConstraintLayout
 *
 */
class RoundCornerConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), IRoundCorner {

    private val roundCornerHelper = RoundCornerHelper()

    init {
        roundCornerHelper.init(context, attrs, this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        roundCornerHelper.onSizeChanged(w, h)
    }


    override fun draw(canvas: Canvas) {
        roundCornerHelper.preDraw(canvas)
        super.draw(canvas)
        roundCornerHelper.afterDraw(canvas)
    }

    override fun setRadius(radiusDp: Float) {
        roundCornerHelper.setRadius(radiusDp)
    }

    override fun setRadius(
        topStartRadiusDp: Float,
        topEndRadiusDp: Float,
        bottomStartRadiusDp: Float,
        bottomEndRadiusDp: Float
    ) {
        roundCornerHelper.setRadius(
            topStartRadiusDp,
            topEndRadiusDp,
            bottomStartRadiusDp,
            bottomEndRadiusDp
        )
    }

    override fun setStrokeWidth(strokeWidth: Float) {
        roundCornerHelper.setStrokeWidth(strokeWidth)
    }

    override fun setStrokeColor(strokeColor: Int) {
        roundCornerHelper.setStrokeColor(strokeColor)
    }

    override fun setStrokeWidthAndColor(strokeWidth: Float, strokeColor: Int) {
        roundCornerHelper.setStrokeWidthAndColor(strokeWidth, strokeColor)
    }

    override fun setStrokeColors(colors: IntArray, positions: FloatArray?) {
        roundCornerHelper.setStrokeColors(colors, positions)
    }

    override fun setStrokeOrientation(orientation: Orientation) {
        roundCornerHelper.setStrokeOrientation(orientation)
    }

    override fun setBgColor(bgColor: Int) {
        roundCornerHelper.setBgColor(bgColor)
    }

    override fun setBgColors(colors: IntArray, positions: FloatArray?) {
        roundCornerHelper.setBgColors(colors, positions)
    }

    override fun setBgOrientation(orientation: Orientation) {
        roundCornerHelper.setBgOrientation(orientation)
    }
}