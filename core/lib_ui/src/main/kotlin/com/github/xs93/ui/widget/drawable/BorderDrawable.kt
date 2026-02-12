package com.github.xs93.ui.widget.drawable

import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/16 17:10
 * @description 自定义Drawable边框,可以实现渐变边框及流动颜色边框
 *
 */
open class BorderDrawable : Drawable {

    companion object {
        const val STYLE_NORMAL_LINEAR = 0
        const val STYLE_NORMAL_SWEEP = 1
        const val STYLE_FLUID_LINEAR = 2
        const val STYLE_FLUID_SWEEP = 3
    }

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

    private var borderStyle: Int = 0

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

    private val fluidMatrix by lazy { Matrix() }
    private var fluidDegree = 0f
        set(value) {
            field = value
            invalidateSelf()
        }

    private var fluidAnim: ObjectAnimator? = null
    private var fluidDuration = 2000L

    private val clearXfermode by lazy {
        PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    constructor(borderMode: Int, borderWidth: Int) : this(
        borderMode,
        borderWidth,
        borderWidth,
        borderWidth,
        borderWidth
    )

    constructor(
        borderMode: Int,
        leftBorderWidth: Int,
        topBorderWidth: Int,
        rightBorderWidth: Int,
        bottomBorderWidth: Int
    ) {
        this.borderStyle = borderMode
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
        this.paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun onStateChange(state: IntArray): Boolean {
        return super.onStateChange(state)
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    open fun createShader(
        contentRectF: RectF,
        colors: IntArray,
        colorPositions: FloatArray? = null
    ): Shader {
        if (colorPositions != null && colors.size != colorPositions.size) {
            throw IllegalArgumentException("colors and colorPositions must have the same length")
        }
        return when (borderStyle) {
            STYLE_NORMAL_SWEEP -> {
                SweepGradient(
                    contentRectF.centerX(),
                    contentRectF.centerY(),
                    colors,
                    colorPositions
                )
            }

            STYLE_FLUID_SWEEP -> {
                SweepGradient(
                    contentRectF.centerX(),
                    contentRectF.centerY(),
                    colors,
                    colorPositions
                ).apply {
                    setLocalMatrix(fluidMatrix)
                }
            }

            STYLE_FLUID_LINEAR -> {
                val shaderRectF =
                    getGradientRectByOrientation(
                        orientation,
                        contentRectF.width(),
                        contentRectF.height()
                    )
                LinearGradient(
                    shaderRectF.left,
                    shaderRectF.top,
                    shaderRectF.right,
                    shaderRectF.bottom,
                    colors,
                    colorPositions,
                    Shader.TileMode.REPEAT
                ).apply {
                    setLocalMatrix(fluidMatrix)
                }
            }

            else -> {
                val shaderRectF =
                    getGradientRectByOrientation(
                        orientation,
                        contentRectF.width(),
                        contentRectF.height()
                    )
                LinearGradient(
                    shaderRectF.left,
                    shaderRectF.top,
                    shaderRectF.right,
                    shaderRectF.bottom,
                    colors,
                    colorPositions,
                    Shader.TileMode.REPEAT
                )
            }
        }
    }

    open fun updateShaderByAnimation(contentRectF: RectF, shader: Shader?) {
        fluidMatrix.reset()
        fluidMatrix.setRotate(fluidDegree, contentRectF.centerX(), contentRectF.centerY())
        shader?.setLocalMatrix(fluidMatrix)
    }

    private fun getGradientRectByOrientation(
        orientation: Orientation,
        width: Float,
        height: Float
    ): RectF {
        val rectF = RectF()
        when (orientation) {
            Orientation.TOP_BOTTOM -> {
                rectF.set(0f, 0f, 0f, height)
            }

            Orientation.TR_BL -> {
                rectF.set(width, 0f, 0f, height)
            }

            Orientation.RIGHT_LEFT -> {
                rectF.set(width, 0f, 0f, 0f)
            }

            Orientation.BR_TL -> {
                rectF.set(width, height, 0f, 0f)
            }

            Orientation.BOTTOM_TOP -> {
                rectF.set(0f, height, 0f, 0f)
            }

            Orientation.BL_TR -> {
                rectF.set(0f, height, width, 0f)
            }

            Orientation.LEFT_RIGHT -> {
                rectF.set(0f, 0f, width, 0f)
            }

            Orientation.TL_BR -> {
                rectF.set(0f, 0f, width, height)
            }
        }
        return rectF
    }

    //<editor-fold desc="公共方法">
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

    //</editor-fold>
    fun setDuration(duration: Long) {
        this.fluidDuration = duration
        if (isFluidRunning()) {
            startFluid()
        }
    }

    /**
     * 开始流动
     */
    fun startFluid() {
        if (borderStyle != STYLE_FLUID_SWEEP && borderStyle != STYLE_FLUID_LINEAR) return
        fluidAnim?.cancel()
        fluidAnim = ObjectAnimator.ofFloat(this, "fluidDegree", 0f, 360f).apply {
            duration = fluidDuration
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            start()
        }
    }

    fun isFluidRunning(): Boolean {
        return fluidAnim?.isRunning == true
    }

    /**
     * 停止流动
     */
    fun cancelFluid() {
        fluidAnim?.cancel()
    }
}