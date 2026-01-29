package com.github.xs93.coil.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ReplacementSpan
import android.view.Gravity
import android.widget.TextView
import androidx.core.text.getSpans
import coil3.Image
import coil3.asDrawable
import coil3.gif.MovieDrawable
import coil3.gif.repeatCount
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.target.Target
import com.github.xs93.coil.CoilManager
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max

class CoilImageSpan(val view: TextView, val url: Any) : ReplacementSpan() {

    /** gif循环次数 */
    private var loopCount: Int = MovieDrawable.REPEAT_INFINITE

    /** 图片宽度 */
    private var drawableWidth: Int = 0

    /** 图片高度 */
    private var drawableHeight: Int = 0

    /** 图片间距 */
    private var drawableMargin: Rect = Rect()

    /** 图片内间距 */
    private var drawablePadding = Rect()

    private var drawableRef: AtomicReference<Drawable> = AtomicReference()

    /** 文字显示区域 */
    private var textDisplayRect = Rect()

    /** 图片原始间距 */
    private var drawableOriginPadding = Rect()

    /** 初始固定图片显示区域, 优先级: 自定义尺寸 > 占位图尺寸 > 文字尺寸 */
    private var fixDrawableBounds = Rect()

    /** 占位图 */
    private var placeHolder: Drawable? = null


    private var requestBuilder: ImageRequest.Builder.() -> Unit = {}

    private fun getDrawable(): Drawable? {
        if (drawableRef.get() == null) {
            val drawableSize = getDrawableSize()
            val target = object : Target {
                override fun onStart(placeholder: Image?) {
                    val drawable = placeholder?.asDrawable(view.resources)
                    if (drawable != null) {
                        drawable.setFixedRatioZoom()
                        placeHolder = drawable
                        drawableRef.set(drawable)
                    }
                }

                override fun onSuccess(result: Image) {
                    super.onSuccess(result)
                    val drawable = result.asDrawable(view.resources)
                    if (drawable is Animatable) {
                        drawable.callback = drawableCallback
                        drawable.start()
                    }
                    if (fixDrawableBounds.isEmpty) {
                        fixDrawableBounds = getDrawableSize()
                    }
                    drawable.bounds = fixDrawableBounds
                    drawableRef.set(drawable)
                    view.invalidate()
                }

                override fun onError(error: Image?) {
                    super.onError(error)
                    val errorDrawable = error?.asDrawable(view.resources)
                    if (errorDrawable != null && errorDrawable != drawableRef.get()) {
                        errorDrawable.setFixedRatioZoom()
                        drawableRef.set(errorDrawable)
                        view.invalidate()
                    }
                }
            }

            val request = ImageRequest.Builder(view.context)
                .data(url)
                .size(drawableSize.width(), drawableSize.height())
                .repeatCount(loopCount)
                .apply(requestBuilder)
                .crossfade(false)
                .target(target)
                .build()
            CoilManager.getImageLoader()?.enqueue(request)
        }
        return drawableRef.get()
    }

    /** 设置等比例缩放图片 */
    private fun Drawable.setFixedRatioZoom() {
        var width = when {
            drawableWidth > 0 -> drawableWidth
            drawableWidth == -1 -> textDisplayRect.width()
            else -> intrinsicWidth
        }
        var height = when {
            drawableHeight > 0 -> drawableHeight
            drawableHeight == -1 -> textDisplayRect.height()
            else -> intrinsicHeight
        }

        getPadding(drawableOriginPadding)
        width += drawablePadding.left + drawablePadding.right + drawableOriginPadding.left + drawableOriginPadding.right
        height += drawablePadding.top + drawablePadding.bottom + drawableOriginPadding.top + drawableOriginPadding.bottom

        if (this is NinePatchDrawable) {
            width = max(width, intrinsicWidth)
            height = max(height, intrinsicHeight)
        }
        bounds.set(0, 0, width, height)
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val fontMetrics = paint.fontMetricsInt
        if (textSize > 0) {
            paint.textSize = textSize.toFloat()
        }
        if (drawableWidth <= 0 || drawableHeight <= 0) {
            val r = Rect()
            paint.getTextBounds(text.toString(), start, end, r)
            val resizeFontMetrics = paint.fontMetricsInt
            textDisplayRect.set(
                0,
                0,
                r.width(),
                resizeFontMetrics.descent - resizeFontMetrics.ascent
            )
        }
        val drawable = getDrawable()
        val bounds = drawable?.bounds ?: getDrawableSize()
        fixDrawableBounds = bounds
        val imageHeight = bounds.height()
        if (fm != null) {
            when (align) {
                Align.CENTER -> {
                    val fontHeight = fontMetrics.descent - fontMetrics.ascent
                    fm.ascent =
                        fontMetrics.ascent - (imageHeight - fontHeight) / 2 - drawableMargin.top
                    fm.descent = fm.ascent + imageHeight + drawableMargin.bottom
                }

                Align.BASELINE -> {
                    fm.ascent =
                        fontMetrics.bottom - imageHeight - fontMetrics.descent - drawableMargin.top - drawableMargin.bottom
                    fm.descent = 0
                }

                Align.BOTTOM -> {
                    fm.ascent =
                        fontMetrics.descent - imageHeight - drawableMargin.top - drawableMargin.bottom
                    fm.descent = 0
                }
            }

            fm.top = fm.ascent
            fm.bottom = fm.descent
        }

        return bounds.right + drawableMargin.left + drawableMargin.right
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val drawable = getDrawable()
        canvas.save()
        val bounds = drawable?.bounds ?: getDrawableSize()
        val transY = when (align) {
            Align.CENTER -> bottom - bounds.bottom - (bottom - top) / 2 + bounds.height() / 2 - drawableMargin.height() / 2
            Align.BASELINE -> bottom - bounds.bottom - paint.fontMetricsInt.descent - drawableMargin.bottom
            Align.BOTTOM -> bottom - bounds.bottom - drawableMargin.bottom
        }
        canvas.translate(x + drawableMargin.left, transY.toFloat())
        drawable?.draw(canvas)

        // draw text
        if (textVisibility) {
            canvas.translate(
                -drawablePadding.width() / 2F - drawableOriginPadding.right,
                -drawablePadding.height() / 2F + drawableOriginPadding.top
            )
            val textWidth = paint.measureText(text, start, end)
            val textDrawRect = Rect()
            val textContainerRect = Rect(bounds)
            Gravity.apply(
                textGravity,
                textWidth.toInt(),
                paint.textSize.toInt(),
                textContainerRect,
                textDrawRect
            )
            if (text is Spanned) {
                // draw text color
                text.getSpans<ForegroundColorSpan>(start, end).lastOrNull()?.let {
                    paint.color = it.foregroundColor
                }
            }
            canvas.drawText(
                text, start, end,
                (textDrawRect.left + textOffset.left - textOffset.right).toFloat() + (drawableOriginPadding.right + drawableOriginPadding.left) / 2,
                (textDrawRect.bottom - paint.fontMetricsInt.descent / 2 + textOffset.top - textOffset.bottom).toFloat() - (drawableOriginPadding.bottom + drawableOriginPadding.top) / 2,
                paint
            )
        }
        canvas.restore()
    }

    /**
     * 默认显示区域
     * 优先使用自定义尺寸, 如果没用配置则使用文字显示区域
     */
    private fun getDrawableSize(): Rect {
        val placeHolder = placeHolder
        var width = when {
            drawableWidth > 0 -> drawableWidth
            drawableWidth == -1 -> textDisplayRect.width()
            placeHolder != null -> placeHolder.intrinsicWidth
            else -> textDisplayRect.width()
        }
        var height = when {
            drawableHeight > 0 -> drawableHeight
            drawableHeight == -1 -> textDisplayRect.height()
            placeHolder != null -> placeHolder.intrinsicHeight
            else -> textDisplayRect.height()
        }
        if (width != placeHolder?.intrinsicWidth) {
            width += drawablePadding.left + drawablePadding.right + drawableOriginPadding.left + drawableOriginPadding.right
        }
        if (height != placeHolder?.intrinsicHeight) {
            height += drawablePadding.top + drawablePadding.bottom + drawableOriginPadding.top + drawableOriginPadding.bottom
        }
        return Rect(0, 0, width, height)
    }

    /** GIF动画触发刷新文字的回调 */
    private val drawableCallback = object : Drawable.Callback {
        override fun invalidateDrawable(who: Drawable) {
            view.invalidate()
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {}

        override fun unscheduleDrawable(who: Drawable, what: Runnable) {}
    }

    enum class Align {
        BASELINE,
        CENTER,
        BOTTOM
    }

    private var align: Align = Align.CENTER

    //<editor-fold desc="Image">
    /**
     * 设置图片垂直对其方式
     * 图片默认垂直居中对齐文字: [Align.CENTER]
     */
    fun setAlign(align: Align) = apply {
        this.align = align
    }

    /**
     * 设置图片宽高
     * 如果指定大于零值则会基于图片宽高中最大值然后根据宽高比例固定缩放图片
     * @param  width 指定图片宽度, -1 使用文字宽度, 0 使用图片原始宽度
     * @param  height 指定图片高度, -1 使用文字高度, 0 使用图片原始高度
     */
    @JvmOverloads
    fun setDrawableSize(width: Int, height: Int = width) = apply {
        this.drawableWidth = width
        this.drawableHeight = height
        drawableRef.set(null)
    }

    /** 设置图片水平间距 */
    @JvmOverloads
    fun setMarginHorizontal(left: Int, right: Int = left) = apply {
        drawableMargin.left = left
        drawableMargin.right = right
    }

    /** 设置图片水平间距 */
    @JvmOverloads
    fun setMarginVertical(top: Int, bottom: Int = top) = apply {
        drawableMargin.top = top
        drawableMargin.bottom = bottom
    }

    /**
     * 设置图片水平内间距
     */
    @JvmOverloads
    fun setPaddingHorizontal(left: Int, right: Int = left) = apply {
        drawablePadding.left = left
        drawablePadding.right = right
        drawableRef.set(null)
    }

    /**
     * 设置图片垂直内间距
     */
    @JvmOverloads
    fun setPaddingVertical(top: Int, bottom: Int = top) = apply {
        drawablePadding.top = top
        drawablePadding.bottom = bottom
        drawableRef.set(null)
    }

    /**
     * 配置Coil请求选项, 例如占位图、加载失败图等
     */
    fun setRequestOption(builder: ImageRequest.Builder.() -> Unit) = apply {
        this.requestBuilder = builder
    }

    /** GIF动画播放循环次数, 默认无限循环 */
    fun setLoopCount(loopCount: Int) = apply {
        this.loopCount = loopCount
    }
    //</editor-fold>

    //<editor-fold desc="Text">
    private var textOffset = Rect()
    private var textGravity = Gravity.CENTER
    private var textVisibility = false
    private var textSize = 0

    /**
     * 当前为背景图片, 这会导致显示文字内容, 但图片不会根据文字内容自动调整
     * @param visibility 是否显示文字
     */
    @JvmOverloads
    fun setTextVisibility(visibility: Boolean = true) = apply {
        textVisibility = visibility
    }

    /**
     * 文字偏移值
     */
    @JvmOverloads
    fun setTextOffset(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) = apply {
        textOffset.set(left, top, right, bottom)
    }

    /**
     * 文字对齐方式(基于图片), 默认对齐方式[Gravity.CENTER]
     * @param gravity 值等效于[TextView.setGravity], 例如[Gravity.BOTTOM], 使用[or]组合多个值
     */
    fun setTextGravity(gravity: Int) = apply {
        this.textGravity = gravity
    }

    /**
     * 配合[AbsoluteSizeSpan]设置字体大小则图片/文字会基线对齐, 而使用本方法则图片/文字会居中对齐
     * @param size 文字大小, 单位px
     * @see setTextVisibility
     */
    fun setTextSize(size: Int) = apply {
        textSize = size
    }
    //</editor-fold>
}