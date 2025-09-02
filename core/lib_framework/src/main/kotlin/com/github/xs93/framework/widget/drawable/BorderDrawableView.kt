package com.github.xs93.framework.widget.drawable

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColorInt
import com.github.xs93.framework.R
import com.github.xs93.framework.widget.drawable.BorderDrawable.Orientation

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/17 15:15
 * @description 使用BorderDrawable作为背景的View，可以简单使用BorderDrawable
 *
 */
class BorderDrawableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var borderDrawable: BorderDrawable
    private var borderStyle = 0
    private var borderWidth = 0
    private var borderLeftWidth = 0
    private var borderTopWidth = 0
    private var borderRightWidth = 0
    private var borderBottomWidth = 0
    private var borderColor = 0
    private var borderColors: IntArray? = null
    private var borderColorsPositions: FloatArray? = null
    private var borderOrientation = Orientation.TOP_BOTTOM
    private var borderInnerRadius = 0f
    private var borderInnerRadiusArray: FloatArray? = null
    private var borderOuterRadius = 0f
    private var borderOuterRadiusArray: FloatArray? = null
    private var useForeground = false
    private var borderAnimDuration = 2000L

    init {
        Log.d("aaaa", "aaa: ")
        context.withStyledAttributes(attrs, R.styleable.BorderDrawableView) {
            borderStyle = getInt(
                R.styleable.BorderDrawableView_border_style,
                BorderDrawable.STYLE_NORMAL_LINEAR
            )
            borderAnimDuration =
                getInteger(R.styleable.BorderDrawableView_border_anim_duration, 2000).toLong()
            borderDrawable = BorderDrawable(borderStyle, borderWidth)

            if (hasValue(R.styleable.BorderDrawableView_border_width)) {
                borderWidth = getDimensionPixelSize(R.styleable.BorderDrawableView_border_width, 0)
                borderLeftWidth = borderWidth
                borderTopWidth = borderWidth
                borderRightWidth = borderWidth
                borderBottomWidth = borderWidth
            }
            if (hasValue(R.styleable.BorderDrawableView_border_left_width)) {
                borderLeftWidth =
                    getDimensionPixelSize(R.styleable.BorderDrawableView_border_left_width, 0)
            }
            if (hasValue(R.styleable.BorderDrawableView_border_top_width)) {
                borderTopWidth =
                    getDimensionPixelSize(R.styleable.BorderDrawableView_border_top_width, 0)
            }
            if (hasValue(R.styleable.BorderDrawableView_border_right_width)) {
                borderRightWidth =
                    getDimensionPixelSize(R.styleable.BorderDrawableView_border_right_width, 0)
            }
            if (hasValue(R.styleable.BorderDrawableView_border_bottom_width)) {
                borderBottomWidth =
                    getDimensionPixelSize(R.styleable.BorderDrawableView_border_bottom_width, 0)
            }

            borderDrawable.setBorderWidth(
                borderLeftWidth,
                borderTopWidth,
                borderRightWidth,
                borderBottomWidth
            )

            if (hasValue(R.styleable.BorderDrawableView_border_color)) {
                borderColor = getColor(R.styleable.BorderDrawableView_border_color, 0)
                borderDrawable.setColor(borderColor)
            }

            if (hasValue(R.styleable.BorderDrawableView_border_colors)) {
                val colorsStr = getString(R.styleable.BorderDrawableView_border_colors)
                borderColors = parseColors(colorsStr)
                borderDrawable.setColors(borderColors)

                if (hasValue(R.styleable.BorderDrawableView_border_colors_positions)) {
                    val positionsStr =
                        getString(R.styleable.BorderDrawableView_border_colors_positions)
                    borderColorsPositions = parseColorsPositions(positionsStr)
                    if (borderColorsPositions != null) {
                        if (borderColorsPositions?.size != borderColors?.size) {
                            throw IllegalArgumentException("border_colors_positions size must be equal to border_colors size")
                        } else {
                            borderDrawable.setColorPositions(borderColorsPositions)
                        }
                    }
                }
            }

            if (hasValue(R.styleable.BorderDrawableView_border_orientation)) {
                val orientation = getInt(R.styleable.BorderDrawableView_border_orientation, 0)
                borderOrientation = when (orientation) {
                    0 -> Orientation.TOP_BOTTOM
                    1 -> Orientation.TR_BL
                    2 -> Orientation.RIGHT_LEFT
                    3 -> Orientation.BR_TL
                    4 -> Orientation.BOTTOM_TOP
                    5 -> Orientation.BL_TR
                    6 -> Orientation.LEFT_RIGHT
                    7 -> Orientation.TL_BR
                    else -> Orientation.TOP_BOTTOM
                }
                borderDrawable.setOrientation(borderOrientation)
            }

            if (hasValue(R.styleable.BorderDrawableView_border_radius)) {
                val borderRadius =
                    getDimensionPixelSize(R.styleable.BorderDrawableView_border_radius, 0)
                borderOuterRadius = borderRadius.toFloat()
                borderInnerRadius = borderRadius.toFloat()
                borderDrawable.setOuterRadius(borderOuterRadius)
                borderDrawable.setInnerRadius(borderInnerRadius)
            }

            if (hasValue(R.styleable.BorderDrawableView_border_outer_radius)) {
                borderOuterRadius =
                    getDimensionPixelSize(R.styleable.BorderDrawableView_border_outer_radius, 0)
                        .toFloat()
                borderDrawable.setOuterRadius(borderOuterRadius)
            }

            if (hasValue(R.styleable.BorderDrawableView_border_inner_radius)) {
                borderInnerRadius =
                    getDimensionPixelSize(R.styleable.BorderDrawableView_border_inner_radius, 0)
                        .toFloat()
                borderDrawable.setInnerRadius(borderInnerRadius)
            }
            if (hasValue(R.styleable.BorderDrawableView_border_outer_radii)) {
                val outerRadiusArrayStr =
                    getString(R.styleable.BorderDrawableView_border_outer_radii)
                borderOuterRadiusArray = parseRadiusArray(outerRadiusArrayStr)
                borderDrawable.setOuterRadii(borderOuterRadiusArray)
            }

            if (hasValue(R.styleable.BorderDrawableView_border_inner_radii)) {
                val innerRadiusArrayStr =
                    getString(R.styleable.BorderDrawableView_border_inner_radii)
                borderInnerRadiusArray = parseRadiusArray(innerRadiusArrayStr)
                borderDrawable.setInnerRadii(borderInnerRadiusArray)
            }
            useForeground = getBoolean(R.styleable.BorderDrawableView_border_use_foreground, false)
            if (useForeground) {
                foreground = borderDrawable
            } else {
                background = borderDrawable
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        borderDrawable.startFluid()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        borderDrawable.cancelFluid()
    }


    private fun parseColors(colorsStr: String?): IntArray? {
        if (colorsStr.isNullOrBlank()) return null
        val colors = colorsStr.split(",")
        if (colors.isEmpty()) return null
        return colors.map {
            it.trim().toColorInt()
        }.toIntArray()
    }


    private fun parseColorsPositions(positionsStr: String?): FloatArray? {
        if (positionsStr.isNullOrBlank()) return null
        val positions = positionsStr.split(",")
        if (positions.isEmpty()) return null
        return positions.map {
            it.trim().toFloat()
        }.toFloatArray()
    }


    private fun parseRadiusArray(radiusArrayStr: String?): FloatArray? {
        if (radiusArrayStr.isNullOrBlank()) return null
        val radiusArray = radiusArrayStr.split(",")
        if (radiusArray.isEmpty()) return null
        if (radiusArray.size != 8) {
            throw IllegalArgumentException(("radii  must be 8 values"))
        }
        return radiusArray.map {
            val dpValue = it.trim().toFloat()
            context.resources.displayMetrics.density * dpValue
        }.toFloatArray()
    }

    fun getBorderDrawable(): BorderDrawable {
        return borderDrawable
    }

    fun setBorderWidth(width: Int) {
        borderWidth = width
        borderLeftWidth = width
        borderTopWidth = width
        borderRightWidth = width
        borderBottomWidth = width
        borderDrawable.setBorderWidth(
            borderLeftWidth,
            borderTopWidth,
            borderRightWidth,
            borderBottomWidth
        )
    }

    fun setBorderWidth(left: Int, top: Int, right: Int, bottom: Int) {
        borderLeftWidth = left
        borderTopWidth = top
        borderRightWidth = right
        borderBottomWidth = bottom
        borderDrawable.setBorderWidth(left, top, right, bottom)
    }

    fun setBorderColor(color: Int) {
        borderColor = color
        borderDrawable.setColor(color)
    }

    fun setBorderColors(colors: IntArray?, colorPositions: FloatArray? = null) {
        borderColors = colors
        borderColorsPositions = colorPositions
        borderDrawable.setColors(colors, colorPositions)
    }

    fun setBorderColorsPositions(colorPositions: FloatArray?) {
        borderColorsPositions = colorPositions
        borderDrawable.setColorPositions(colorPositions)
    }

    fun setBorderOrientation(orientation: Orientation) {
        borderOrientation = orientation
        borderDrawable.setOrientation(orientation)
    }

    fun setBorderOuterRadius(outerRadius: Float) {
        borderOuterRadius = outerRadius
        borderDrawable.setOuterRadius(outerRadius)
    }

    fun setBorderOuterRadii(outerRadii: FloatArray?) {
        borderOuterRadiusArray = outerRadii
        borderDrawable.setOuterRadii(outerRadii)
    }

    fun setBorderInnerRadius(innerRadius: Float) {
        borderInnerRadius = innerRadius
        borderDrawable.setInnerRadius(innerRadius)
    }

    fun setBorderInnerRadii(innerRadii: FloatArray?) {
        borderInnerRadiusArray = innerRadii
        borderDrawable.setInnerRadii(innerRadii)
    }

    fun setDuration(duration: Long) {
        borderAnimDuration = duration
        borderDrawable.setDuration(duration)
    }
}