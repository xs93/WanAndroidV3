package com.github.xs93.roundcorner

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColorInt
import com.github.xs93.gdview.R

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/10/31 13:59
 * @description
 *  #FF0000,#ffb0b0,#FFFF00,#0000FF,#00FFFF
 */
@SuppressLint("ObsoleteSdkInt")
class RoundCornerHelper : IRoundCorner {
    private var context: Context? = null
    private var view: View? = null

    private var viewWidth = 0
    private var viewHeight = 0

    private var topStartRadius = 0f
    private var topEndRadius = 0f
    private var bottomStartRadius = 0f
    private var bottomEndRadius = 0f


    private var strokeWidth = 0f
    private var strokeColor = Color.WHITE
    private var strokeColors: IntArray? = null
    private var strokeColorPositions: FloatArray? = null
    private var strokeColorsOrientation: Orientation = Orientation.LEFT_RIGHT
    private var strokeShader: LinearGradient? = null

    private val orientRectF = RectF()
    private val contentRectF = RectF()
    private val contentRadii = FloatArray(8)
    private val strokeRectF = RectF()
    private val strokeRadii = FloatArray(8)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val path = Path()
    private val tempPath = Path()

    private val xferMode: PorterDuffXfermode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    } else {
        PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    private var saveLayoutId = -1

    fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, view: View) {
        this.context = context
        this.view = view
        if (view is ViewGroup && view.background == null) {
            view.setBackgroundColor(Color.TRANSPARENT)
        }
        context.withStyledAttributes(attrs, R.styleable.RoundCornerAttr, defStyleAttr) {
            if (hasValue(R.styleable.RoundCornerAttr_rc_radius)) {
                val radius = getDimension(R.styleable.RoundCornerAttr_rc_radius, 0f)
                topStartRadius = radius
                topEndRadius = radius
                bottomStartRadius = radius
                bottomEndRadius = radius
            }
            if (hasValue(R.styleable.RoundCornerAttr_rc_topStartRadius)) {
                topStartRadius =
                    getDimension(R.styleable.RoundCornerAttr_rc_topStartRadius, 0f)
            }

            if (hasValue(R.styleable.RoundCornerAttr_rc_topEndRadius)) {
                topEndRadius =
                    getDimension(R.styleable.RoundCornerAttr_rc_topEndRadius, 0f)
            }

            if (hasValue(R.styleable.RoundCornerAttr_rc_bottomStartRadius)) {
                bottomStartRadius =
                    getDimension(R.styleable.RoundCornerAttr_rc_bottomStartRadius, 0f)
            }

            if (hasValue(R.styleable.RoundCornerAttr_rc_bottomEndRadius)) {
                bottomEndRadius =
                    getDimension(R.styleable.RoundCornerAttr_rc_bottomEndRadius, 0f)
            }

            if (hasValue(R.styleable.RoundCornerAttr_rc_strokeWidth)) {
                strokeWidth = getDimension(R.styleable.RoundCornerAttr_rc_strokeWidth, 0f)
            }

            if (hasValue(R.styleable.RoundCornerAttr_rc_strokeColor)) {
                strokeColor = getColor(R.styleable.RoundCornerAttr_rc_strokeColor, Color.WHITE)
            }

            val colors = parseColorArrays(this, R.styleable.RoundCornerAttr_rc_strokeColors)
            val colorPositions =
                parseColorPositions(this, R.styleable.RoundCornerAttr_rc_strokeColorsPositions)

            if (colors != null && colors.size > 1) {
                strokeColors = colors
            } else if (colors != null && colors.size == 1) {
                strokeColor = colors[0]
                strokeColors = null
            } else {
                strokeColors = null
            }
            strokeColorPositions = colorPositions
            val colorSize = strokeColors?.size ?: 0
            val positionSize = colorPositions?.size ?: 0
            if (colorSize > 0 && positionSize > 0 && colorSize != positionSize) {
                throw IllegalArgumentException("strokeColors and strokeColorsPositions size must be equal")
            }
            strokeColorsOrientation =
                parseOrientation(this, R.styleable.RoundCornerAttr_rc_strokeColorsOrientation)
        }
    }


    fun onSizeChanged(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
        initRadii()

        val halfStrokeWidth = strokeWidth / 2f
        orientRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        val dp1 = dp2Px(2f, context)
        orientRectF.inset(-dp1, -dp1)
        contentRectF.set(
            strokeWidth,
            strokeWidth,
            width - strokeWidth,
            height - strokeWidth
        )
        strokeRectF.set(
            halfStrokeWidth,
            halfStrokeWidth,
            width - halfStrokeWidth,
            height - halfStrokeWidth
        )
        val tempStrokeColor = strokeColors
        strokeShader = if (strokeWidth > 0 && tempStrokeColor != null) {
            val rectF = getGradientRectByOrientation(
                strokeColorsOrientation,
                width.toFloat(),
                height.toFloat()
            )
            LinearGradient(
                rectF.left,
                rectF.top,
                rectF.right,
                rectF.bottom,
                tempStrokeColor,
                strokeColorPositions,
                Shader.TileMode.CLAMP
            )
        } else {
            null
        }
    }

    fun preDraw(canvas: Canvas) {
        if (needRoundCorner()) {
            saveLayoutId = canvas.saveLayer(orientRectF, null)
        }
    }

    fun afterDraw(canvas: Canvas) {
        if (needRoundCorner()) {
            paint.style = Paint.Style.FILL
            paint.xfermode = xferMode
            path.reset()
            path.addRoundRect(contentRectF, contentRadii, Path.Direction.CCW)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tempPath.reset()
                tempPath.addRect(orientRectF, Path.Direction.CCW)
                tempPath.op(path, Path.Op.DIFFERENCE)
                canvas.drawPath(tempPath, paint)
            } else {
                canvas.drawPath(path, paint)
            }
            if (saveLayoutId != -1) {
                canvas.restoreToCount(saveLayoutId)
                saveLayoutId = -1
            }
        }
        if (strokeWidth > 0) {
            paint.xfermode = null
            paint.style = Paint.Style.STROKE
            paint.color = strokeColor
            paint.strokeWidth = strokeWidth
            paint.shader = strokeShader
            path.reset()
            path.addRoundRect(strokeRectF, strokeRadii, Path.Direction.CCW)
            canvas.drawPath(path, paint)
        }
    }


    private fun needRoundCorner(): Boolean {
        return topStartRadius > 0 || topEndRadius > 0 || bottomStartRadius > 0 || bottomEndRadius > 0
    }

    private fun initRadii() {
        contentRadii[0] = topStartRadius - strokeWidth
        contentRadii[1] = topStartRadius - strokeWidth
        contentRadii[2] = topEndRadius - strokeWidth
        contentRadii[3] = topEndRadius - strokeWidth
        contentRadii[4] = bottomEndRadius - strokeWidth
        contentRadii[5] = bottomEndRadius - strokeWidth
        contentRadii[6] = bottomStartRadius - strokeWidth
        contentRadii[7] = bottomStartRadius - strokeWidth
        strokeRadii[0] = topStartRadius - strokeWidth / 2f
        strokeRadii[1] = topStartRadius - strokeWidth / 2f
        strokeRadii[2] = topEndRadius - strokeWidth / 2f
        strokeRadii[3] = topEndRadius - strokeWidth / 2f
        strokeRadii[4] = bottomEndRadius - strokeWidth / 2f
        strokeRadii[5] = bottomEndRadius - strokeWidth / 2f
        strokeRadii[6] = bottomStartRadius - strokeWidth / 2f
        strokeRadii[7] = bottomStartRadius - strokeWidth / 2f
    }

    private fun dp2Px(dp: Float, context: Context?): Float {
        val displayMetrics =
            context?.resources?.displayMetrics ?: Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
    }

    private fun parseColorArrays(typedArray: TypedArray, index: Int): IntArray? {
        val context = context ?: return null
        if (!typedArray.hasValue(index)) return null
        val resourceId = typedArray.getResourceId(index, -1)
        if (resourceId != -1) {
            val colorsTypeArray = context.resources.obtainTypedArray(resourceId)
            if (colorsTypeArray.length() == 0) {
                colorsTypeArray.recycle()
                return null
            }
            val colors = IntArray(colorsTypeArray.length())
            for (i in colors.indices) {
                colors[i] = try {
                    colorsTypeArray.getColor(i, Color.BLACK)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Color.BLACK
                }
            }
            colorsTypeArray.recycle()
            return colors
        } else {
            val colorsStr = typedArray.getString(index)
            if (colorsStr.isNullOrBlank()) {
                return null
            }
            val colors = colorsStr.split(",")
            if (colors.isEmpty()) return null
            val values = IntArray(colors.size)
            for (i in values.indices) {
                values[i] = try {
                    colors[i].trim().toColorInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Color.BLACK
                }
            }
            return values
        }
    }

    private fun parseColorPositions(typedArray: TypedArray, index: Int): FloatArray? {
        val context = context ?: return null
        if (!typedArray.hasValue(index)) return null
        val resourceId = typedArray.getResourceId(index, -1)
        if (resourceId != -1) {
            val positionsTypeArray = context.resources.obtainTypedArray(resourceId)
            if (positionsTypeArray.length() == 0) {
                positionsTypeArray.recycle()
                return null
            }
            val positions =
                (0 until positionsTypeArray.length()).mapNotNull {
                    positionsTypeArray.getFloat(it, 0f)
                }
            positionsTypeArray.recycle()
            return positions.toFloatArray()
        } else {
            val positionsStr = typedArray.getString(index)
            if (positionsStr.isNullOrBlank()) {
                return null
            }
            val positions = positionsStr.split(",")
            if (positions.isEmpty()) return null
            return positions.map { it.trim().toFloat() }.toFloatArray()
        }
    }

    private fun parseOrientation(typedArray: TypedArray, index: Int): Orientation {
        if (!typedArray.hasValue(index)) return Orientation.LEFT_RIGHT
        return when (typedArray.getInt(index, 0)) {
            0 -> Orientation.LEFT_RIGHT
            1 -> Orientation.TL_BR
            2 -> Orientation.TOP_BOTTOM
            3 -> Orientation.TR_BL
            4 -> Orientation.RIGHT_LEFT
            5 -> Orientation.BR_TL
            6 -> Orientation.BOTTOM_TOP
            7 -> Orientation.BL_TR
            else -> Orientation.LEFT_RIGHT
        }
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

    //region 接口实现
    override fun setRadius(radiusDp: Float) {
        val radius = dp2Px(radiusDp, context)
        topStartRadius = radius
        topEndRadius = radius
        bottomStartRadius = radius
        bottomEndRadius = radius
        view?.let {
            onSizeChanged(viewWidth, viewHeight)
            it.invalidate()
        }
    }

    override fun setRadius(
        topStartRadiusDp: Float,
        topEndRadiusDp: Float,
        bottomStartRadiusDp: Float,
        bottomEndRadiusDp: Float
    ) {
        topStartRadius = dp2Px(topStartRadiusDp, context)
        topEndRadius = dp2Px(topEndRadiusDp, context)
        bottomStartRadius = dp2Px(bottomStartRadiusDp, context)
        bottomEndRadius = dp2Px(bottomEndRadiusDp, context)
        view?.let {
            onSizeChanged(viewWidth, viewHeight)
            it.invalidate()
        }
    }

    override fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = dp2Px(strokeWidth, context)
        view?.let {
            onSizeChanged(viewWidth, viewHeight)
            it.invalidate()
        }
    }

    override fun setStrokeColor(strokeColor: Int) {
        this.strokeColor = strokeColor
        view?.invalidate()
    }

    override fun setStrokeWidthAndColor(strokeWidth: Float, strokeColor: Int) {
        this.strokeWidth = dp2Px(strokeWidth, context)
        this.strokeColor = strokeColor
        view?.let {
            onSizeChanged(viewWidth, viewHeight)
            it.invalidate()
        }
    }

    override fun setStrokeColors(colors: IntArray, positions: FloatArray?) {
        if (colors.size <= 1) {
            throw IllegalArgumentException("colors size must > 1")
        }
        if (positions != null && colors.size != positions.size) {
            throw IllegalArgumentException("colors size must = positions size")
        }
        this.strokeColors = colors
        this.strokeColorPositions = positions
        view?.let {
            onSizeChanged(viewWidth, viewHeight)
            it.invalidate()
        }
    }

    override fun setStrokeOrientation(orientation: Orientation) {
        this.strokeColorsOrientation = orientation
        view?.let {
            onSizeChanged(viewWidth, viewHeight)
            it.invalidate()
        }
    }
    //endregion

}