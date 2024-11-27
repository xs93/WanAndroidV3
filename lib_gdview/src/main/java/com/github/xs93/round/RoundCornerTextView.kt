package com.github.xs93.round

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.github.xs93.gdview.R

/**
 * 圆角TextView
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/15 9:27
 * @email 466911254@qq.com
 */
class RoundCornerTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val roundCornerViewImpl: RoundCornerViewImpl = RoundCornerViewImpl(
        this,
        context,
        attrs,
        R.styleable.RoundCornerTextView,
        intArrayOf(
            R.styleable.RoundCornerTextView_roundViewClip,
            R.styleable.RoundCornerTextView_roundViewIsCircle,
            R.styleable.RoundCornerTextView_roundViewRadius,
            R.styleable.RoundCornerTextView_roundViewLeftTopRadius,
            R.styleable.RoundCornerTextView_roundViewRightTopRadius,
            R.styleable.RoundCornerTextView_roundViewRightBottomRadius,
            R.styleable.RoundCornerTextView_roundViewLeftBottomRadius,
            R.styleable.RoundCornerTextView_roundViewStrokeWidth,
            R.styleable.RoundCornerTextView_roundViewStrokeColor,
            R.styleable.RoundCornerTextView_roundViewBgColor,
            R.styleable.RoundCornerTextView_roundViewBgDrawable,
            R.styleable.RoundCornerTextView_roundViewBgStartColor,
            R.styleable.RoundCornerTextView_roundViewBgCenterColor,
            R.styleable.RoundCornerTextView_roundViewBgEndColor,
            R.styleable.RoundCornerTextView_roundViewGradientOrientation,
        )
    )

    override fun draw(canvas: Canvas) {
        roundCornerViewImpl.beforeDispatchDraw(canvas)
        super.draw(canvas)
        roundCornerViewImpl.afterDispatchDraw(canvas)
    }
}