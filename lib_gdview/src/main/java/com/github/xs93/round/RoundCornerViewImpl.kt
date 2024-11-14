package com.github.xs93.round

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewOutlineProvider

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/14 13:28
 * @email 466911254@qq.com
 */
class RoundCornerViewImpl(
    val view: View,
    val context: Context,
    attributeSet: AttributeSet?,
    attrs: IntArray,
    attrIndex: IntArray
) : IRoundCornerView {

    private val TAG = "RoundCornerViewImpl"

    private var isClipChild: Boolean = false
    private var isCircle: Boolean = false
    private var radius = 0f
    private var leftTopRadius: Float = 0f
    private var rightTopRadius: Float = 0f
    private var rightBottomRadius: Float = 0f
    private var leftBottomRadius: Float = 0f
    private var strokeWidth: Float = 0f
    private var strokeColor: Int = 0
    private var bgColor: Int = 0
    private var bgDrawable: Drawable? = null
    private var bgStartColor: Int = 0
    private var bgEndColor: Int = 0
    private var bgCenterColor: Int = 0
    private var bgOrientation: Int = 0

    private val rectF = RectF()
    private val clipPath: Path = Path()
    private var outlineCanClip: Boolean = false
    private var saveCanvasId: Int? = null

    private val contentRadii = FloatArray(8)
    private val strokeRadii = FloatArray(8)

    private val strokePaint = Paint(Paint.DITHER_FLAG or Paint.ANTI_ALIAS_FLAG)
    private val strokePath = Path()
    private val strokeRectF = RectF()

    init {
        initialize(context, attributeSet, attrs, attrIndex)
        strokePaint.apply {
            style = Paint.Style.STROKE
        }
    }

    private fun initialize(
        context: Context,
        attributeSet: AttributeSet?,
        attrs: IntArray,
        attrIndex: IntArray
    ) {
        val typedArray = context.obtainStyledAttributes(attributeSet, attrs)
        isClipChild = typedArray.getBoolean(attrIndex[0], false)
        isCircle = typedArray.getBoolean(attrIndex[1], false)
        radius = typedArray.getDimension(attrIndex[2], 0f)
        leftTopRadius = typedArray.getDimension(attrIndex[3], 0f)
        rightTopRadius = typedArray.getDimension(attrIndex[4], 0f)
        rightBottomRadius = typedArray.getDimension(attrIndex[5], 0f)
        leftBottomRadius = typedArray.getDimension(attrIndex[6], 0f)
        strokeWidth = typedArray.getDimension(attrIndex[7], 0f)
        strokeColor = typedArray.getColor(attrIndex[8], 0)
        bgColor = typedArray.getColor(attrIndex[9], Color.TRANSPARENT)
        bgDrawable = typedArray.getDrawable(attrIndex[10])
        bgStartColor = typedArray.getColor(attrIndex[11], -1001)
        bgCenterColor = typedArray.getColor(attrIndex[12], -1001)
        bgEndColor = typedArray.getColor(attrIndex[13], -1001)
        bgOrientation = typedArray.getInt(attrIndex[14], 0)
        typedArray.recycle()
        val drawable = bgDrawable ?: buildGradientDrawable()
        view.background = drawable

        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: android.graphics.Outline) {
                if (needClip()) {
                    setupClipPath(view.width, view.height)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        outline.setPath(clipPath)
                    } else {
                        @Suppress("DEPRECATION")
                        outline.setConvexPath(clipPath)
                    }
                    outlineCanClip = outline.canClip()
                } else {
                    outline.setRect(0, 0, view.width, view.height)
                }
                Log.d(TAG, "getOutline: $outlineCanClip")
            }
        }
    }

    override fun beforeDispatchDraw(canvas: Canvas?) {
        if (needClip() && !outlineCanClip) {
            saveCanvasId = canvas?.save()
            canvas?.clipPath(clipPath)
        }
    }

    override fun afterDispatchDraw(canvas: Canvas?) {
        saveCanvasId?.let {
            canvas?.restoreToCount(it)
        }
    }

    private fun buildGradientDrawable(): Drawable? {
        val gd: GradientDrawable// 创建drawable
        val orientation: GradientDrawable.Orientation =
            when (bgOrientation) {
                0 -> GradientDrawable.Orientation.TOP_BOTTOM
                1 -> GradientDrawable.Orientation.TR_BL
                2 -> GradientDrawable.Orientation.RIGHT_LEFT
                3 -> GradientDrawable.Orientation.BR_TL
                4 -> GradientDrawable.Orientation.BOTTOM_TOP
                5 -> GradientDrawable.Orientation.BL_TR
                6 -> GradientDrawable.Orientation.LEFT_RIGHT
                else -> GradientDrawable.Orientation.TL_BR
            }
        if (bgStartColor != -1001 && bgEndColor != -1001) {
            val colors = if (bgCenterColor != -1001) {
                intArrayOf(bgStartColor, bgCenterColor, bgEndColor)
            } else {
                intArrayOf(bgStartColor, bgEndColor)
            }
            gd = GradientDrawable(orientation, colors)
        } else {
            gd = GradientDrawable()
            gd.setColor(bgColor)
        }
        if (isCircle) {
            gd.shape = GradientDrawable.OVAL
        } else {
            gd.shape = GradientDrawable.RECTANGLE
        }
        initContentRadii()
        gd.cornerRadii = contentRadii
        if (strokeWidth > 0) {
            gd.setStroke(strokeWidth.toInt(), strokeColor)
        }
        return gd
    }

    private fun initContentRadii() {
        if (radius > 0f) {
            contentRadii[0] = radius
            contentRadii[1] = radius
            contentRadii[2] = radius
            contentRadii[3] = radius
            contentRadii[4] = radius
            contentRadii[5] = radius
            contentRadii[6] = radius
            contentRadii[7] = radius

        } else {
            contentRadii[0] = leftTopRadius
            contentRadii[1] = leftTopRadius
            contentRadii[2] = rightTopRadius
            contentRadii[3] = rightTopRadius
            contentRadii[4] = rightBottomRadius
            contentRadii[5] = rightBottomRadius
            contentRadii[6] = leftBottomRadius
            contentRadii[7] = leftBottomRadius
        }
    }

    private fun initStrokeRadii(strokeWidth: Float) {
        val halfStrokeWidth = strokeWidth / 2f
        if (radius > 0f) {
            val realRadius = (radius - halfStrokeWidth).coerceAtLeast(0f)
            strokeRadii[0] = realRadius
            strokeRadii[1] = realRadius
            strokeRadii[2] = realRadius
            strokeRadii[3] = realRadius
            strokeRadii[4] = realRadius
            strokeRadii[5] = realRadius
            strokeRadii[6] = realRadius
            strokeRadii[7] = realRadius

        } else {
            val realLeftTopRadius = (leftTopRadius - halfStrokeWidth).coerceAtLeast(0f)
            val realRightTopRadius = (rightTopRadius - halfStrokeWidth).coerceAtLeast(0f)
            val realRightBottomRadius = (rightBottomRadius - halfStrokeWidth).coerceAtLeast(0f)
            val realLeftBottomRadius = (leftBottomRadius - halfStrokeWidth).coerceAtLeast(0f)
            strokeRadii[0] = realLeftTopRadius
            strokeRadii[1] = realLeftTopRadius
            strokeRadii[2] = realRightTopRadius
            strokeRadii[3] = realRightTopRadius
            strokeRadii[4] = realRightBottomRadius
            strokeRadii[5] = realRightBottomRadius
            strokeRadii[6] = realLeftBottomRadius
            strokeRadii[7] = realLeftBottomRadius
        }
    }


    private fun needClip(): Boolean {
        return isClipChild
    }

    private fun setupClipPath(width: Int, height: Int) {
        clipPath.reset()
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        if (isCircle) {
            clipPath.addOval(rectF, Path.Direction.CCW)
        } else {
            initContentRadii()
            clipPath.addRoundRect(rectF, contentRadii, Path.Direction.CCW)
        }
    }

    private fun setupStrokePath(width: Int, height: Int) {
        strokePath.reset()
        val halfStrokeWidth = strokeWidth / 2
        strokeRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        strokeRectF.inset(halfStrokeWidth, halfStrokeWidth)
        if (isCircle) {
            strokePath.addOval(strokeRectF, Path.Direction.CCW)
        } else {
            initStrokeRadii(strokeWidth)
            strokePath.addRoundRect(strokeRectF, strokeRadii, Path.Direction.CCW)
        }
    }
}