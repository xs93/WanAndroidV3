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
class RoundedCornerFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val roundCornerViewImpl: RoundCornerViewImpl = RoundCornerViewImpl(
        this,
        context,
        attrs,
        R.styleable.RoundedCornerFrameLayout,
        intArrayOf(
            R.styleable.RoundedCornerFrameLayout_roundViewClip,
            R.styleable.RoundedCornerFrameLayout_roundViewIsCircle,
            R.styleable.RoundedCornerFrameLayout_roundViewRadius,
            R.styleable.RoundedCornerFrameLayout_roundViewLeftTopRadius,
            R.styleable.RoundedCornerFrameLayout_roundViewRightTopRadius,
            R.styleable.RoundedCornerFrameLayout_roundViewRightBottomRadius,
            R.styleable.RoundedCornerFrameLayout_roundViewLeftBottomRadius,
            R.styleable.RoundedCornerFrameLayout_roundViewStrokeWidth,
            R.styleable.RoundedCornerFrameLayout_roundViewStrokeColor,
            R.styleable.RoundedCornerFrameLayout_roundViewBgColor,
            R.styleable.RoundedCornerFrameLayout_roundViewBgDrawable,
            R.styleable.RoundedCornerFrameLayout_roundViewBgStartColor,
            R.styleable.RoundedCornerFrameLayout_roundViewBgCenterColor,
            R.styleable.RoundedCornerFrameLayout_roundViewBgEndColor,
            R.styleable.RoundedCornerFrameLayout_roundViewGradientOrientation,
        )
    )

    override fun dispatchDraw(canvas: Canvas) {
        roundCornerViewImpl.beforeDispatchDraw(canvas)
        super.dispatchDraw(canvas)
        roundCornerViewImpl.afterDispatchDraw(canvas)
    }
}