package com.github.xs93.framework.widget.progress

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withSave
import com.github.xs93.framework.R

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/28 15:29
 * @description 自定义进度条View
 *
 */
class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_PROGRESS_WIDTH = 20
        private const val DEFAULT_WIDTH = 300
        private const val DEFAULT_HEIGHT = 300
        private const val TAG = "ProgressView"
    }

    enum class ProgressStyle {
        LINEAR,
        CIRCLE
    }

    private var style: ProgressStyle = ProgressStyle.LINEAR
    private var progressWidth: Int = DEFAULT_PROGRESS_WIDTH
        set(value) {
            field = value
            progressPaint.strokeWidth = value.toFloat()
            backgroundPaint.strokeWidth = value.toFloat()
            postInvalidate()
        }
    private var progressBackgroundColor: Int = 0
        set(value) {
            field = value
            backgroundPaint.color = value
            postInvalidate()
        }
    private var progressColor: Int = 0
        set(value) {
            field = value
            progressPaint.color = value
            postInvalidate()
        }


    private var needResetShader: Boolean = false

    private var progressColors: IntArray? = null
        set(value) {
            field = value
            needResetShader = true
            postInvalidate()
        }

    private var progressColorPositions: FloatArray? = null
    private var startAngle: Int = 0
    private var radius: Float = 0f

    private var progressMax: Int = 100
    private var progress: Int = 0

    private var tempRectF = RectF()

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.ProgressView) {
            val styleValue = getInt(R.styleable.ProgressView_progress_style, 0)
            style = getStyle(styleValue)

            val capStyleValue = getInt(R.styleable.ProgressView_progress_cap, 0)
            setCapStyle(getPainCap(capStyleValue))

            progressWidth = getDimensionPixelSize(
                R.styleable.ProgressView_progress_width,
                DEFAULT_PROGRESS_WIDTH
            )

            progressBackgroundColor =
                getColor(R.styleable.ProgressView_progress_background_color, Color.GRAY)

            progressColor = getColor(R.styleable.ProgressView_progress_color, getPrimaryColor())

            if (hasValue(R.styleable.ProgressView_progress_colors)) {
                val resourceId = getResourceId(R.styleable.ProgressView_progress_colors, 0)
                val colorsArray: IntArray?
                if (resourceId != 0) {
                    val typedArray = resources.obtainTypedArray(resourceId)
                    val colors = parasColorArrays(typedArray)
                    typedArray.recycle()
                    colorsArray = colors
                } else {
                    val colorsStr = getString(R.styleable.ProgressView_progress_colors)
                    colorsArray = parseColors(colorsStr)
                }
                if (colorsArray != null && colorsArray.size < 2) {
                    throw IllegalArgumentException("progress_colors size must >=2")
                }
                progressColors = colorsArray
            }
            if (hasValue(R.styleable.ProgressView_progress_colors_positions)) {
                val resourceId =
                    getResourceId(R.styleable.ProgressView_progress_colors_positions, 0)
                val colorsPositionsArray: FloatArray?
                if (resourceId != 0) {
                    val typedArray = resources.obtainTypedArray(resourceId)
                    val positionList =
                        (0 until typedArray.length()).mapNotNull { typedArray.getFloat(it, 0f) }
                    typedArray.recycle()
                    colorsPositionsArray = positionList.toFloatArray()
                } else {
                    val colorsPositionsStr =
                        getString(R.styleable.ProgressView_progress_colors_positions)
                    colorsPositionsArray = parseColorsPositions(colorsPositionsStr)
                }
                if (colorsPositionsArray != null) {
                    if (progressColors != null && colorsPositionsArray.size != progressColors!!.size) {
                        throw IllegalArgumentException("progress_colors_positions size must equal progress_colors size")
                    }
                }
                progressColorPositions = colorsPositionsArray
            }

            if (hasValue(R.styleable.ProgressView_progress_start_angle)) {
                val startAngleValue = getInt(R.styleable.ProgressView_progress_start_angle, 0)
                startAngle = getStartAngleValue(startAngleValue)
            }
            progressMax = getInt(R.styleable.ProgressView_progress_max, 100)
            if (progressMax <= 0) {
                progressMax = 100
            }
            progress = getInt(R.styleable.ProgressView_progress, 0)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width: Int
        var height: Int

        if (style == ProgressStyle.LINEAR) {
            width = when (widthMode) {
                MeasureSpec.EXACTLY -> {
                    widthSize
                }

                MeasureSpec.AT_MOST,
                MeasureSpec.UNSPECIFIED -> {
                    DEFAULT_WIDTH
                }

                else -> {
                    DEFAULT_WIDTH
                }
            }
            height = progressWidth
            setMeasuredDimension(width, height)
        } else {
            width = when (widthMode) {
                MeasureSpec.EXACTLY -> {
                    widthSize
                }

                MeasureSpec.AT_MOST,
                MeasureSpec.UNSPECIFIED -> {
                    DEFAULT_WIDTH
                }

                else -> {
                    DEFAULT_WIDTH
                }
            }
            height = when (heightMode) {
                MeasureSpec.EXACTLY -> {
                    heightSize
                }

                MeasureSpec.AT_MOST,
                MeasureSpec.UNSPECIFIED -> {
                    DEFAULT_HEIGHT
                }

                else -> {
                    DEFAULT_HEIGHT
                }
            }
            val realSize = width.coerceAtMost(height)
            setMeasuredDimension(realSize, realSize)
            radius = realSize / 2f - progressWidth / 2f
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (style == ProgressStyle.LINEAR) {
            drawLinearProgress(canvas)
        } else {
            drawCircleProgress(canvas)
        }
    }

    private fun drawLinearProgress(canvas: Canvas) {
        val capStyle = progressPaint.strokeCap
        var capDrawWidth = if (capStyle == Paint.Cap.BUTT) 0f else progressWidth / 2f
        canvas.drawLine(
            capDrawWidth,
            measuredHeight / 2f,
            measuredWidth.toFloat() - capDrawWidth,
            measuredHeight / 2f,
            backgroundPaint
        )
        if (needResetShader) {
            needResetShader = false
            val colors = progressColors
            if (colors != null) {
                progressPaint.shader = LinearGradient(
                    capDrawWidth,
                    measuredHeight / 2f,
                    measuredWidth.toFloat() - capDrawWidth,
                    measuredHeight / 2f,
                    colors,
                    progressColorPositions,
                    Shader.TileMode.CLAMP
                )
            }
        }

        if (progress > 0) {
            val ratio = (progress.toFloat() / progressMax.toFloat()).coerceIn(0f, 1f)
            val showProgressWidth = (measuredWidth - capDrawWidth * 2) * ratio + capDrawWidth
            canvas.drawLine(
                capDrawWidth,
                measuredHeight / 2f,
                showProgressWidth,
                measuredHeight / 2f,
                progressPaint
            )
        }
    }


    private fun drawCircleProgress(canvas: Canvas) {
        val ratio = (progress.toFloat() / progressMax.toFloat()).coerceIn(0f, 1f)
        val sweepAngle = ratio * 360f
        canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, radius, backgroundPaint)
        tempRectF.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        tempRectF.inset(progressWidth / 2f, progressWidth / 2f)
        if (needResetShader) {
            needResetShader = false
            val colors = progressColors
            if (colors != null) {
                val shader = SweepGradient(
                    tempRectF.centerX(),
                    tempRectF.centerY(),
                    colors,
                    progressColorPositions,
                )
                val matrix = Matrix().apply {
                    postRotate(startAngle.toFloat(), tempRectF.centerX(), tempRectF.centerY())
                }
                shader.setLocalMatrix(matrix)
                progressPaint.shader = shader
            }
        }
        canvas.withSave {
            canvas.drawArc(tempRectF, startAngle.toFloat(), sweepAngle, false, progressPaint)
        }
    }

    private fun getPrimaryColor(): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        return typedValue.data
    }

    private fun parasColorArrays(typedArray: TypedArray): IntArray {
        val values = IntArray(typedArray.length())
        (0 until typedArray.length()).forEach {
            val typedValue = TypedValue()
            if (typedArray.getValue(it, typedValue)) {
                if (typedValue.type == TypedValue.TYPE_ATTRIBUTE) {
                    val attrResId = typedValue.data
                    val attrTypedValue = TypedValue()
                    if (context.theme.resolveAttribute(attrResId, attrTypedValue, true)) {
                        values[it] = attrTypedValue.data
                    } else {
                        values[it] = Color.BLACK
                    }
                } else if (typedValue.type == TypedValue.TYPE_REFERENCE) {
                    val resourceId = typedValue.data
                    if (resourceId != 0) {
                        val attrTypedValue = TypedValue()
                        if (context.theme.resolveAttribute(resourceId, attrTypedValue, true)) {
                            values[it] = attrTypedValue.data
                        } else {
                            values[it] = Color.BLACK
                        }
                    } else {
                        values[it] = Color.BLACK
                    }
                } else if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                    values[it] = typedValue.data
                } else {
                    values[it] = Color.BLACK
                }
            } else {
                values[it] = Color.BLACK
            }
        }
        return values
    }

    private fun getStyle(styleValue: Int): ProgressStyle {
        if (styleValue == 1) return ProgressStyle.CIRCLE
        return ProgressStyle.LINEAR
    }

    private fun getPainCap(capStyleValue: Int): Paint.Cap {
        return if (capStyleValue == 1) {
            Paint.Cap.ROUND
        } else if (capStyleValue == 2) {
            Paint.Cap.SQUARE
        } else {
            Paint.Cap.BUTT
        }
    }

    private fun getStartAngleValue(startAngleValue: Int): Int {
        return when (startAngleValue) {
            -1 -> 0
            -2 -> 90
            -3 -> 180
            -4 -> 270
            else -> startAngleValue.coerceAtLeast(0)
        }
    }

    private fun parseColors(colorsStr: String?): IntArray? {
        if (colorsStr.isNullOrBlank()) return null
        val colors = colorsStr.split(",")
        if (colors.isEmpty()) return null
        return colors.filter { it.isNotBlank() }.map { it.trim().toColorInt() }.toIntArray()
    }

    private fun parseColorsPositions(positionsStr: String?): FloatArray? {
        if (positionsStr.isNullOrBlank()) return null
        val positions = positionsStr.split(",")
        if (positions.isEmpty()) return null
        return positions.mapNotNull {
            it.trim().toFloatOrNull()
        }.toFloatArray()
    }

    fun setCapStyle(cap: Paint.Cap) {
        backgroundPaint.strokeCap = cap
        progressPaint.strokeCap = cap
        invalidate()
    }
}