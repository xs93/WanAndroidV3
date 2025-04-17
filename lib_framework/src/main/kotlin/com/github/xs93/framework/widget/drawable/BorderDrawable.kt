package com.github.xs93.framework.widget.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/16 17:10
 * @description 自定义Drawable边框,可以实现渐变边框
 *
 */
open class BorderDrawable : Drawable {

    /**
     * 渐变颜色方向
     */
    enum class Orientation {
        TOP_BOTTOM,
        TR_BL,
        RIGHT_LEFT,
        BR_TL,
        BOTTOM_TOP,
        BL_TR,
        LEFT_RIGHT,
        TL_BR
    }

    private var leftBorderWidth = 0
    private var topBorderWidth = 0
    private var rightBorderWidth = 0
    private var bottomBorderWidth = 0

    private var outerRadius: Float = 0f
    private var outerRadii: FloatArray? = null

    private var innerRadius: Float = 0f
    private var innerRadii: FloatArray? = null

    private val outerPath = Path()
    private val innerPath = Path()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private var color = 0
    private var colors: IntArray? = null
    private var colorPositions: FloatArray? = null

    private var boundsWidth: Int? = null
    private var boundsHeight: Int? = null

    private var orientation: Orientation = Orientation.TOP_BOTTOM

    private val drawableRectF = RectF()
    private val innerRectF = RectF()

    private var colorsChanged = false
    private var orientationChanged = false
    private var radiusChanged = false
    private var borderWidthChanged = false


    private val clearXfermode by lazy {
        PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    constructor(borderWidth: Int) : this(borderWidth, borderWidth, borderWidth, borderWidth)

    constructor(
        leftBorderWidth: Int,
        topBorderWidth: Int,
        rightBorderWidth: Int,
        bottomBorderWidth: Int
    ) {
        this.leftBorderWidth = leftBorderWidth
        this.topBorderWidth = topBorderWidth
        this.rightBorderWidth = rightBorderWidth
        this.bottomBorderWidth = bottomBorderWidth
    }

    override fun draw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()
        if (boundsWidth == null || boundsHeight == null ||
            width != boundsWidth || height != boundsHeight ||
            radiusChanged
        ) {
            drawableRectF.set(0f, 0f, width.toFloat(), height.toFloat())
            innerRectF.set(
                leftBorderWidth.toFloat(),
                topBorderWidth.toFloat(),
                width - rightBorderWidth.toFloat(),
                height - bottomBorderWidth.toFloat()
            )
            outerPath.reset()
            innerPath.reset()
            if (outerRadii != null) {
                outerPath.addRoundRect(drawableRectF, outerRadii!!, Path.Direction.CW)
            } else {
                outerPath.addRoundRect(drawableRectF, outerRadius, outerRadius, Path.Direction.CW)
            }
            if (innerRadii != null) {
                innerPath.addRoundRect(innerRectF, innerRadii!!, Path.Direction.CW)
            } else {
                innerPath.addRoundRect(innerRectF, innerRadius, innerRadius, Path.Direction.CW)
            }
            boundsWidth = width
            boundsHeight = height
        }

        if (colorsChanged || orientationChanged) {
            if (colors != null) {
                paint.shader = createShader(drawableRectF, colors!!, colorPositions)
            } else {
                paint.shader = null
                paint.color = color
            }
        }

        updateShaderByAnimation(drawableRectF, paint.shader)

        val layerId = canvas.saveLayer(drawableRectF, null)
        canvas.drawPath(outerPath, paint)
        paint.xfermode = clearXfermode
        canvas.drawPath(innerPath, paint)
        paint.xfermode = null
        canvas.restoreToCount(layerId)

        colorsChanged = false
        orientationChanged = false
        radiusChanged = false
    }

    override fun setAlpha(alpha: Int) {
        this.paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun onStateChange(state: IntArray): Boolean {
        return super.onStateChange(state)
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    private fun getGradientRectByOrientation(
        width: Float,
        height: Float
    ): RectF {
        val rectF = RectF()
        when (orientation) {
            Orientation.TOP_BOTTOM -> {
                rectF.set(0f, 0f, 0f, height.toFloat())
            }

            Orientation.TR_BL -> {
                rectF.set(width.toFloat(), 0f, 0f, height.toFloat())
            }

            Orientation.RIGHT_LEFT -> {
                rectF.set(width.toFloat(), 0f, 0f, 0f)
            }

            Orientation.BR_TL -> {
                rectF.set(width.toFloat(), height.toFloat(), 0f, 0f)
            }

            Orientation.BOTTOM_TOP -> {
                rectF.set(0f, height.toFloat(), 0f, 0f)
            }

            Orientation.BL_TR -> {
                rectF.set(0f, height.toFloat(), width.toFloat(), 0f)
            }

            Orientation.LEFT_RIGHT -> {
                rectF.set(0f, 0f, width.toFloat(), 0f)
            }

            Orientation.TL_BR -> {
                rectF.set(0f, 0f, width.toFloat(), height.toFloat())
            }
        }
        return rectF
    }

    open fun createShader(
        contentRectF: RectF,
        colors: IntArray,
        colorPositions: FloatArray? = null
    ): Shader {
        if (colorPositions != null && colors.size != colorPositions.size) {
            throw IllegalArgumentException("colors and colorPositions must have the same length")
        }
        val shaderRectF = getGradientRectByOrientation(contentRectF.width(), contentRectF.height())
        return LinearGradient(
            shaderRectF.left,
            shaderRectF.top,
            shaderRectF.right,
            shaderRectF.bottom,
            colors,
            colorPositions,
            Shader.TileMode.REPEAT
        )
    }

    open fun updateShaderByAnimation(contentRectF: RectF, shader: Shader?) {

    }

    fun setOuterRadius(outerRadius: Float) {
        this.outerRadius = outerRadius
        radiusChanged = true
        invalidateSelf()
    }

    fun setOuterRadii(outerRadii: FloatArray?) {
        this.outerRadii = outerRadii
        radiusChanged = true
        invalidateSelf()
    }

    fun setInnerRadius(innerRadius: Float) {
        this.innerRadius = innerRadius
        radiusChanged = true
        invalidateSelf()
    }

    fun setInnerRadii(innerRadii: FloatArray?) {
        this.innerRadii = innerRadii
        radiusChanged = true
        invalidateSelf()
    }

    fun setColor(color: Int) {
        this.color = color
        colorsChanged = true
        invalidateSelf()
    }

    fun setColors(colors: IntArray?, colorPositions: FloatArray? = null) {
        this.colors = colors
        this.colorPositions = colorPositions
        colorsChanged = true
        invalidateSelf()
    }

    fun setColorPositions(colorPositions: FloatArray?) {
        this.colorPositions = colorPositions
        colorsChanged = true
        invalidateSelf()
    }

    fun setOrientation(orientation: Orientation) {
        this.orientation = orientation
        orientationChanged = true
        invalidateSelf()
    }

    fun setBorderWidth(left: Int, top: Int, right: Int, bottom: Int) {
        this.leftBorderWidth = left
        this.topBorderWidth = top
        this.rightBorderWidth = right
        this.bottomBorderWidth = bottom
        borderWidthChanged = true
        invalidateSelf()
    }

    fun setBorderWidth(width: Int) {
        this.leftBorderWidth = width
        this.topBorderWidth = width
        this.rightBorderWidth = width
        this.bottomBorderWidth = width
        borderWidthChanged = true
        invalidateSelf()
    }
}