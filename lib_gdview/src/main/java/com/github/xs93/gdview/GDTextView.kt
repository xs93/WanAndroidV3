package com.github.xs93.gdview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/9 9:55
 * @email 466911254@qq.com
 */
class GDTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    private var bgColor: Int = 0
    private var borderWidth: Int = 0
    private var borderColor: Int = Color.BLACK
    private var startColor: Int = -1
    private var centerColor: Int = -1
    private var endColor: Int = -1
    private var gradientOrientation: Int = 0
    private var bgDrawable: Drawable? = null
    private var colorsStr: String? = null
    private var radius: Float = 0f
    private var leftTopRadius: Float = 0f
    private var leftBottomRadius: Float = 0f
    private var rightTopRadius: Float = 0f
    private var rightBottomRadius: Float = 0f

    private var colors: IntArray? = null

    init {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.GDTextView, defStyleAttr, 0)
        bgDrawable = attributes.getDrawable(R.styleable.GDTextView_gdBgDrawable)
        bgColor = attributes.getColor(R.styleable.GDTextView_gdBgColor, Color.TRANSPARENT)
        borderWidth = attributes.getDimensionPixelSize(R.styleable.GDTextView_gdBorderWidth, 0)
        borderColor = attributes.getColor(R.styleable.GDTextView_gdBorderColor, Color.BLACK)
        startColor = attributes.getColor(R.styleable.GDTextView_gdStartColor, -1)
        centerColor = attributes.getColor(R.styleable.GDTextView_gdCenterColor, -1)
        endColor = attributes.getColor(R.styleable.GDTextView_gdEndColor, -1)
        colorsStr = attributes.getString(R.styleable.GDTextView_gdColors)
        gradientOrientation = attributes.getInt(R.styleable.GDTextView_gdGradientOrientation, 0)
        radius = attributes.getDimension(R.styleable.GDTextView_gdRadius, 0f)
        leftTopRadius = attributes.getDimension(R.styleable.GDTextView_gdLeftTopRadius, 0f)
        leftBottomRadius = attributes.getDimension(R.styleable.GDTextView_gdLeftBottomRadius, 0f)
        rightTopRadius = attributes.getDimension(R.styleable.GDTextView_gdRightTopRadius, 0f)
        rightBottomRadius = attributes.getDimension(R.styleable.GDTextView_gdRightBottomRadius, 0f)

        colors = getColorArray()

        attributes.recycle()
        if (bgDrawable == null) {
            bgDrawable = buildGradientDrawable()
        }
        background = bgDrawable
    }

    private fun buildGradientDrawable(): Drawable {
        val gd: GradientDrawable// 创建drawable
        if (colors != null && colors!!.isNotEmpty()) {
            val orientation: GradientDrawable.Orientation =
                when (gradientOrientation) {
                    0 -> GradientDrawable.Orientation.TOP_BOTTOM
                    1 -> GradientDrawable.Orientation.TR_BL
                    2 -> GradientDrawable.Orientation.RIGHT_LEFT
                    3 -> GradientDrawable.Orientation.BR_TL
                    4 -> GradientDrawable.Orientation.BOTTOM_TOP
                    5 -> GradientDrawable.Orientation.BL_TR
                    6 -> GradientDrawable.Orientation.LEFT_RIGHT
                    else -> GradientDrawable.Orientation.TL_BR

                }
            gd = GradientDrawable(orientation, colors)
        } else {
            gd = GradientDrawable()
            gd.setColor(bgColor)
        }
        gd.shape = GradientDrawable.RECTANGLE
        gd.cornerRadii = getRadiiArray()
        if (borderWidth > 0) {
            gd.setStroke(borderWidth, borderColor)
        }
        return gd
    }


    private fun getColorArray(): IntArray {
        val colorsList = colorsStr!!.split(",").map { Color.parseColor(it) }
        if (colorsList.isNotEmpty()) {
            return colorsList.toIntArray()
        }
        val colors = mutableListOf<Int>()
        if (startColor != -1) {
            colors.add(startColor)
        }
        if (centerColor != -1) {
            colors.add(centerColor)
        }
        if (endColor != -1) {
            colors.add(endColor)
        }
        return colors.toIntArray()
    }


    private fun getRadiiArray(): FloatArray {
        return if (radius > 0f) {
            floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
        } else {
            floatArrayOf(
                leftTopRadius, leftTopRadius,
                leftBottomRadius, leftBottomRadius,
                rightBottomRadius, rightBottomRadius,
                rightTopRadius, rightTopRadius
            )
        }
    }

    fun setBgColor(bgColor: Int) {
        this.bgColor = bgColor
        bgDrawable = buildGradientDrawable()
        background = bgDrawable
    }

    fun setBorderWidth(borderWidth: Int) {
        this.borderWidth = borderWidth
        bgDrawable = buildGradientDrawable()
        background = bgDrawable
    }

    fun setBorderColor(borderColor: Int) {
        this.borderColor = borderColor
        bgDrawable = buildGradientDrawable()
        background = bgDrawable
    }

    fun setRadius(radius: Float) {
        this.radius = radius
        bgDrawable = buildGradientDrawable()
        background = bgDrawable
    }

    fun setRadius(leftTopRadius: Float, leftBottomRadius: Float, rightBottomRadius: Float, rightTopRadius: Float) {
        this.leftTopRadius = leftTopRadius
        this.leftBottomRadius = leftBottomRadius
        this.rightBottomRadius = rightBottomRadius
        this.rightTopRadius = rightTopRadius
        bgDrawable = buildGradientDrawable()
        background = bgDrawable
    }

    fun setColors(colors: IntArray) {
        this.colors = colors
        bgDrawable = buildGradientDrawable()
        background = bgDrawable
    }
}