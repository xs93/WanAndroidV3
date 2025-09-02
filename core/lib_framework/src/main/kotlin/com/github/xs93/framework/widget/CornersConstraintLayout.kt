package com.github.xs93.framework.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withClip
import com.github.xs93.framework.R

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/6/16 11:00
 * @description 可自定义圆角的Layout,使用Clip裁剪
 *
 */
class CornersConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val clipPath = Path()

    private var startTopRadius = 0f
    private var endTopRadius = 0f
    private var endBottomRadius = 0f
    private var startBottomRadius = 0f

    private var radiusChanged: Boolean = false

    init {
        context.withStyledAttributes(attrs, R.styleable.CornersConstraintLayout) {
            val radius = getDimension(R.styleable.CornersConstraintLayout_ccl_radius, 0f)
            startTopRadius = radius
            endTopRadius = radius
            endBottomRadius = radius
            startBottomRadius = radius
            if (hasValue(R.styleable.CornersConstraintLayout_ccl_start_top_radius)) {
                startTopRadius =
                    getDimension(R.styleable.CornersConstraintLayout_ccl_start_top_radius, 0f)
            }
            if (hasValue(R.styleable.CornersConstraintLayout_ccl_end_top_radius)) {
                endTopRadius =
                    getDimension(R.styleable.CornersConstraintLayout_ccl_end_top_radius, 0f)
            }
            if (hasValue(R.styleable.CornersConstraintLayout_ccl_end_bottom_radius)) {
                endBottomRadius =
                    getDimension(R.styleable.CornersConstraintLayout_ccl_end_bottom_radius, 0f)
            }
            if (hasValue(R.styleable.CornersConstraintLayout_ccl_start_bottom_radius)) {
                startBottomRadius =
                    getDimension(R.styleable.CornersConstraintLayout_ccl_start_bottom_radius, 0f)
            }
        }
        radiusChanged = true
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (radiusChanged) {
            var leftTopRadius = startTopRadius
            var rightTopRadius = endTopRadius
            var rightBottomRadius = endBottomRadius
            var leftBottomRadius = startBottomRadius
            if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                leftTopRadius = endTopRadius
                rightTopRadius = startTopRadius
                rightBottomRadius = startBottomRadius
                leftBottomRadius = endBottomRadius
            }
            val radiusArray = floatArrayOf(
                leftTopRadius, leftTopRadius,
                rightTopRadius, rightTopRadius,
                rightBottomRadius, rightBottomRadius,
                leftBottomRadius, leftBottomRadius
            )
            clipPath.reset()
            clipPath.addRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                radiusArray,
                Path.Direction.CW
            )
            radiusChanged = false
        }
        canvas.withClip(clipPath) {
            super.dispatchDraw(canvas)
        }
    }

    fun setRadius(radius: Float) {
        startTopRadius = radius
        endTopRadius = radius
        endBottomRadius = radius
        startBottomRadius = radius
        radiusChanged = true
        invalidate()
    }

    fun setRadius(
        startTopRadius: Float,
        endTopRadius: Float,
        endBottomRadius: Float,
        startBottomRadius: Float
    ) {
        this.startTopRadius = startTopRadius
        this.endTopRadius = endTopRadius
        this.endBottomRadius = endBottomRadius
        this.startBottomRadius = startBottomRadius
        radiusChanged = true
        invalidate()
    }
}