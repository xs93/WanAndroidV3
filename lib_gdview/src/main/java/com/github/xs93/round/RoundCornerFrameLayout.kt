package com.github.xs93.round

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.xs93.gdview.R

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/14 11:28
 * @email 466911254@qq.com
 */
class RoundCornerFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val roundCornerViewImpl: RoundCornerViewImpl = RoundCornerViewImpl(
        this,
        context,
        attrs,
        R.styleable.RoundCornerFrameLayout,
        intArrayOf(
            R.styleable.RoundCornerFrameLayout_roundViewClip,
            R.styleable.RoundCornerFrameLayout_roundViewIsCircle,
            R.styleable.RoundCornerFrameLayout_roundViewRadius,
            R.styleable.RoundCornerFrameLayout_roundViewLeftTopRadius,
            R.styleable.RoundCornerFrameLayout_roundViewRightTopRadius,
            R.styleable.RoundCornerFrameLayout_roundViewRightBottomRadius,
            R.styleable.RoundCornerFrameLayout_roundViewLeftBottomRadius,
            R.styleable.RoundCornerFrameLayout_roundViewStrokeWidth,
            R.styleable.RoundCornerFrameLayout_roundViewStrokeColor,
            R.styleable.RoundCornerFrameLayout_roundViewBgColor,
            R.styleable.RoundCornerFrameLayout_roundViewBgDrawable,
            R.styleable.RoundCornerFrameLayout_roundViewBgStartColor,
            R.styleable.RoundCornerFrameLayout_roundViewBgCenterColor,
            R.styleable.RoundCornerFrameLayout_roundViewBgEndColor,
            R.styleable.RoundCornerFrameLayout_roundViewGradientOrientation,
        )
    )

    override fun dispatchDraw(canvas: Canvas) {
        roundCornerViewImpl.beforeDispatchDraw(canvas)
        super.dispatchDraw(canvas)
        roundCornerViewImpl.afterDispatchDraw(canvas)
    }
}