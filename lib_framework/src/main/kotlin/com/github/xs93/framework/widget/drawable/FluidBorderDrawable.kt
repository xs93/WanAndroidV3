package com.github.xs93.framework.widget.drawable

import android.animation.ObjectAnimator
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.view.animation.LinearInterpolator

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/17 17:06
 * @description 流动渐变的Drawable
 *
 */
class FluidBorderDrawable : BorderDrawable {


    constructor(borderWidth: Int) : super(borderWidth)
    constructor(
        leftBorderWidth: Int,
        topBorderWidth: Int,
        rightBorderWidth: Int,
        bottomBorderWidth: Int
    ) : super(leftBorderWidth, topBorderWidth, rightBorderWidth, bottomBorderWidth)


    private val matrix = Matrix()
    private var degree = 0f
        set(value) {
            field = value
            invalidateSelf()
        }

    private var fluidAnim: ObjectAnimator? = null

    override fun createShader(
        contentRect: RectF,
        colors: IntArray,
        colorPositions: FloatArray?
    ): Shader {
        val shader =
            SweepGradient(contentRect.centerX(), contentRect.centerY(), colors, colorPositions)
        shader.setLocalMatrix(matrix)
        return shader
    }

    override fun updateShaderByAnimation(contentRectF: RectF, shader: Shader?) {
        matrix.reset()
        matrix.setRotate(degree, contentRectF.centerX(), contentRectF.centerY())
        shader?.setLocalMatrix(matrix)
    }

    /**
     * 开始流动
     */
    fun startFluid() {
        fluidAnim?.cancel()
        fluidAnim = ObjectAnimator.ofFloat(this, "degree", 0f, 360f).apply {
            duration = 2000L
            interpolator = LinearInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            start()
        }
    }

    /**
     * 停止流动
     */
    fun cancelFluid() {
        fluidAnim?.cancel()
    }
}