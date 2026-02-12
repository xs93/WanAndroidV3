package com.github.xs93.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * 内容铺满，从顶部开始裁剪内容的ImageView
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/13 17:16
 * @email 466911254@qq.com
 */
class TopClipContentImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        scaleType = ScaleType.MATRIX
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        resetImageMatrix()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        resetImageMatrix()
        return super.setFrame(l, t, r, b)
    }

    private fun resetImageMatrix() {
        if (scaleType == ScaleType.MATRIX) {
            val drawable = drawable ?: return
            val matrix = imageMatrix
            val scale: Float
            val viewWidth = width - paddingLeft - paddingRight
            val viewHeight = height - paddingTop - paddingBottom
            val drawableWidth = drawable.intrinsicWidth
            val drawableHeight = drawable.intrinsicHeight

            scale = if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
                viewHeight.toFloat() / drawableHeight.toFloat()
            } else {
                viewWidth.toFloat() / drawableWidth.toFloat()
            }
            val scaledWidth = scale * drawableWidth
            val translateWidth = (scaledWidth - viewWidth) / 2f
            matrix.setScale(scale, scale)
            matrix.postTranslate(-translateWidth, 0f)
            imageMatrix = matrix
        }
    }
}