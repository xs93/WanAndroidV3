package com.github.xs93.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes

/**
 * 描边文字
 * 注意:此控件的Gravity只能设置为center，否则文字会偏移
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/2/27 9:21
 * @email 466911254@qq.com
 */
class StrokedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs) {

    private val borderTextView: AppCompatTextView = AppCompatTextView(context, attrs)
    private var strokeWidth = 0f
    private var strokeColor = Color.TRANSPARENT

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val borderText = borderTextView.text
        if (borderText == null || borderText != text) {
            borderTextView.text = text
            postInvalidate()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        val height = measuredHeight
        val newWidth = (width + strokeWidth).toInt()
        val newHeight = (height + strokeWidth).toInt()
        setMeasuredDimension(newWidth, newHeight)
        val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY)
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY)
        borderTextView.measure(newWidthMeasureSpec, newHeightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        borderTextView.layout(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        if (borderTextView.layoutDirection != layoutDirection) {
            borderTextView.layoutDirection = layoutDirection
        }
        if (borderTextView.textDirection != textDirection) {
            borderTextView.textDirection = textDirection
        }
        if (borderTextView.text != text) {
            borderTextView.text = text
        }
        borderTextView.paint.textSize = paint.textSize
        borderTextView.draw(canvas)
        canvas.translate(strokeWidth / 2f, 0f)
        super.onDraw(canvas)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        borderTextView.layoutParams = params
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        super.layoutDirection = layoutDirection
        borderTextView.layoutDirection = layoutDirection
    }

    override fun setTextDirection(textDirection: Int) {
        super.textDirection = textDirection
        borderTextView.textDirection = textDirection
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.StrokedTextView) {
            strokeWidth = getDimension(R.styleable.StrokedTextView_stroke_width, 0f)
            strokeColor = getColor(R.styleable.StrokedTextView_stroke_color, Color.TRANSPARENT)
        }
        gravity = Gravity.CENTER

        borderTextView.textDirection = textDirection
        borderTextView.layoutDirection = layoutDirection
        borderTextView.gravity = Gravity.CENTER
        borderTextView.setTextColor(strokeColor)
        val borderPaint = borderTextView.paint
        borderPaint.strokeWidth = strokeWidth
        borderPaint.style = Paint.Style.STROKE
        borderPaint.color = strokeColor
    }
}