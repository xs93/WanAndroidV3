package com.github.xs93.round

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.xs93.gdview.R

/**
 * 圆角ConstraintLayout
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/15 9:33
 * @email 466911254@qq.com
 */
class RoundCornerConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val roundCornerViewImpl: RoundCornerViewImpl = RoundCornerViewImpl(
        this,
        context,
        attrs,
        R.styleable.RoundCornerConstraintLayout,
        intArrayOf(
            R.styleable.RoundCornerConstraintLayout_roundViewClip,
            R.styleable.RoundCornerConstraintLayout_roundViewIsCircle,
            R.styleable.RoundCornerConstraintLayout_roundViewRadius,
            R.styleable.RoundCornerConstraintLayout_roundViewLeftTopRadius,
            R.styleable.RoundCornerConstraintLayout_roundViewRightTopRadius,
            R.styleable.RoundCornerConstraintLayout_roundViewRightBottomRadius,
            R.styleable.RoundCornerConstraintLayout_roundViewLeftBottomRadius,
            R.styleable.RoundCornerConstraintLayout_roundViewStrokeWidth,
            R.styleable.RoundCornerConstraintLayout_roundViewStrokeColor,
            R.styleable.RoundCornerConstraintLayout_roundViewBgColor,
            R.styleable.RoundCornerConstraintLayout_roundViewBgDrawable,
            R.styleable.RoundCornerConstraintLayout_roundViewBgStartColor,
            R.styleable.RoundCornerConstraintLayout_roundViewBgCenterColor,
            R.styleable.RoundCornerConstraintLayout_roundViewBgEndColor,
            R.styleable.RoundCornerConstraintLayout_roundViewGradientOrientation,
        )
    )

    override fun dispatchDraw(canvas: Canvas) {
        roundCornerViewImpl.beforeDispatchDraw(canvas)
        super.dispatchDraw(canvas)
        roundCornerViewImpl.afterDispatchDraw(canvas)
    }
}