package com.github.xs93.widget.roundcorner.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout
import com.github.xs93.widget.roundcorner.IRoundCorner
import com.github.xs93.widget.roundcorner.RoundCornerHelper

class RoundCornerFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), IRoundCorner {

    override val roundCornerHelper = RoundCornerHelper()

    init {
        roundCornerHelper.init(context, attrs, this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        roundCornerHelper.onSizeChanged(w, h)
    }

    override fun draw(canvas: Canvas) {
        roundCornerHelper.preDraw(canvas)
        super.draw(canvas)
        roundCornerHelper.afterDraw(canvas)
    }
}
