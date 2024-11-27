package com.github.xs93.round

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import com.github.xs93.gdview.R

/**
 * 圆角LinearLayout
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/15 9:31
 * @email 466911254@qq.com
 */
class RoundCornerLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val roundCornerViewImpl: RoundCornerViewImpl = RoundCornerViewImpl(
        this,
        context,
        attrs,
        R.styleable.RoundCornerLinearLayout,
        intArrayOf(
            R.styleable.RoundCornerLinearLayout_roundViewClip,
            R.styleable.RoundCornerLinearLayout_roundViewIsCircle,
            R.styleable.RoundCornerLinearLayout_roundViewRadius,
            R.styleable.RoundCornerLinearLayout_roundViewLeftTopRadius,
            R.styleable.RoundCornerLinearLayout_roundViewRightTopRadius,
            R.styleable.RoundCornerLinearLayout_roundViewRightBottomRadius,
            R.styleable.RoundCornerLinearLayout_roundViewLeftBottomRadius,
            R.styleable.RoundCornerLinearLayout_roundViewStrokeWidth,
            R.styleable.RoundCornerLinearLayout_roundViewStrokeColor,
            R.styleable.RoundCornerLinearLayout_roundViewBgColor,
            R.styleable.RoundCornerLinearLayout_roundViewBgDrawable,
            R.styleable.RoundCornerLinearLayout_roundViewBgStartColor,
            R.styleable.RoundCornerLinearLayout_roundViewBgCenterColor,
            R.styleable.RoundCornerLinearLayout_roundViewBgEndColor,
            R.styleable.RoundCornerLinearLayout_roundViewGradientOrientation,
        )
    )

    override fun dispatchDraw(canvas: Canvas) {
        roundCornerViewImpl.beforeDispatchDraw(canvas)
        super.dispatchDraw(canvas)
        roundCornerViewImpl.afterDispatchDraw(canvas)
    }
}