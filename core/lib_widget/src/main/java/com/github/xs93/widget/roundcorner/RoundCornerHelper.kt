package com.github.xs93.widget.roundcorner

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
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toColorInt
import com.github.xs93.widget.R

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
    private var isRlt: Boolean = false

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
    private var drawStroke: Boolean = true
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private var bgColors: IntArray? = null
    private var bgColorPositions: FloatArray? = null
    private var bgColorsOrientation: Orientation = Orientation.LEFT_RIGHT

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

    fun init(context: Context, attrs: AttributeSet?, view: View) {
        this.context = context
        this.view = view
        context.withStyledAttributes(attrs, R.styleable.RoundCornerAttr) {
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

            val parseBgColors = parseColorArrays(this, R.styleable.RoundCornerAttr_rc_bgColors)
            val parseBgColorPositions =
                parseColorPositions(this, R.styleable.RoundCornerAttr_rc_bgColorsPositions)
            bgColors = parseBgColors
            bgColorPositions = parseBgColorPositions
            val parseBgColorsSize = bgColors?.size ?: 0
            val parseBgColorPositionsSize = bgColorPositions?.size ?: 0
            if (parseBgColorsSize > 1 && parseBgColorPositionsSize > 1 && parseBgColorsSize != parseBgColorPositionsSize) {
                throw IllegalArgumentException("bgColors and bgColorsPositions size must be equal")
            }
            bgColorsOrientation =
                parseOrientation(this, R.styleable.RoundCornerAttr_rc_bgColorsOrientation)

            drawStroke = getBoolean(R.styleable.RoundCornerAttr_rc_drawStroke, true)
        }
        initRadii()
        buildBgGradientDrawable(view)
    }


    fun onSizeChanged(width: Int, height: Int) {
        this.viewWidth = width
        this.viewHeight = height
        initRadii()
        val halfStrokeWidth = strokeWidth / 2f
        orientRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        val dp1 = dp2Px(2f, context)
        orientRectF.inset(-dp1, -dp1)
        contentRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        strokeRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        strokeRectF.inset(halfStrokeWidth, halfStrokeWidth)
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

        if (strokeWidth > 0 && drawStroke) {
            strokePaint.xfermode = null
            strokePaint.style = Paint.Style.STROKE
            strokePaint.color = strokeColor
            strokePaint.strokeWidth = strokeWidth
            strokePaint.shader = strokeShader
            path.reset()
            path.addRoundRect(strokeRectF, strokeRadii, Path.Direction.CCW)
            canvas.drawPath(path, strokePaint)
        }
    }


    private fun needRoundCorner(): Boolean {
        return topStartRadius > 0 || topEndRadius > 0 || bottomStartRadius > 0 || bottomEndRadius > 0
    }

    private fun initRadii() {
        isRlt = view?.layoutDirection == View.LAYOUT_DIRECTION_RTL
        val topLeftRadius = if (isRlt) topEndRadius else topStartRadius
        val topRightRadius = if (isRlt) topStartRadius else topEndRadius
        val bottomLeftRadius = if (isRlt) bottomEndRadius else bottomStartRadius
        val bottomRightRadius = if (isRlt) bottomStartRadius else bottomEndRadius

        contentRadii[0] = topLeftRadius
        contentRadii[1] = topLeftRadius
        contentRadii[2] = topRightRadius
        contentRadii[3] = topRightRadius
        contentRadii[4] = bottomRightRadius
        contentRadii[5] = bottomRightRadius
        contentRadii[6] = bottomLeftRadius
        contentRadii[7] = bottomLeftRadius
        strokeRadii[0] = topLeftRadius - strokeWidth / 2f
        strokeRadii[1] = topLeftRadius - strokeWidth / 2f
        strokeRadii[2] = topRightRadius - strokeWidth / 2f
        strokeRadii[3] = topRightRadius - strokeWidth / 2f
        strokeRadii[4] = bottomRightRadius - strokeWidth / 2f
        strokeRadii[5] = bottomRightRadius - strokeWidth / 2f
        strokeRadii[6] = bottomLeftRadius - strokeWidth / 2f
        strokeRadii[7] = bottomLeftRadius - strokeWidth / 2f
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


    private fun buildBgGradientDrawable(view: View) {
        //已经存在背景,则不在处理自定义背景
        val bgDrawable = view.background
        if (bgDrawable != null) return
        val colors = bgColors
        val positions = bgColorPositions
        if (colors == null) { //ViewGroup需要设置透明背景,防止stroke绘制无效
            if (view is ViewGroup) {
                view.setBackgroundColor(Color.TRANSPARENT)
            }
            return
        }

        val gdDrawable: GradientDrawable
        val orientation = when (bgColorsOrientation) {
            Orientation.LEFT_RIGHT -> GradientDrawable.Orientation.LEFT_RIGHT
            Orientation.TL_BR -> GradientDrawable.Orientation.TL_BR
            Orientation.TOP_BOTTOM -> GradientDrawable.Orientation.TOP_BOTTOM
            Orientation.TR_BL -> GradientDrawable.Orientation.TR_BL
            Orientation.RIGHT_LEFT -> GradientDrawable.Orientation.RIGHT_LEFT
            Orientation.BR_TL -> GradientDrawable.Orientation.BR_TL
            Orientation.BOTTOM_TOP -> GradientDrawable.Orientation.BOTTOM_TOP
            Orientation.BL_TR -> GradientDrawable.Orientation.BL_TR
        }
        if (colors.size == 1) {
            gdDrawable = GradientDrawable()
            gdDrawable.setColor(colors[0])
        } else {
            gdDrawable = GradientDrawable()
            gdDrawable.orientation = orientation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                gdDrawable.setColors(colors, positions)
            } else {
                gdDrawable.colors = colors
            }
        }
        gdDrawable.cornerRadii = floatArrayOf(
            topStartRadius,
            topStartRadius,
            topEndRadius,
            topEndRadius,
            bottomEndRadius,
            bottomEndRadius,
            bottomStartRadius,
            bottomStartRadius
        )
        gdDrawable.shape = GradientDrawable.RECTANGLE
        view.background = gdDrawable
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

    override fun setBgColor(bgColor: Int) {
        bgColors = intArrayOf(bgColor)
        view?.let {
            buildBgGradientDrawable(it)
        }
    }

    override fun setBgColors(colors: IntArray, positions: FloatArray?) {
        if (colors.size <= 1) {
            throw IllegalArgumentException("colors size must > 1")
        }
        if (positions != null && colors.size != positions.size) {
            throw IllegalArgumentException("colors size must = positions size")
        }
        this.bgColors = colors
        this.bgColorPositions = positions
        view?.let {
            buildBgGradientDrawable(it)
        }
    }

    override fun setBgOrientation(orientation: Orientation) {
        this.bgColorsOrientation = orientation
        view?.let {
            buildBgGradientDrawable(it)
        }
    }
    //endregion

}