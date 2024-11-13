package com.github.xs93.framework.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import com.github.xs93.framework.R
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * 解决嵌套RecyclerView滑动冲突
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/13 16:17
 * @email 466911254@qq.com
 */
class NestedScrollableRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f

    // 可能多层嵌套,所以通过代码指定父布局滑动方向
    private var parentOrientation: Int = 0

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        val ta = context.obtainStyledAttributes(attrs, R.styleable.NestedScrollableRecyclerView)
        parentOrientation =
            ta.getInt(R.styleable.NestedScrollableRecyclerView_parentOrientation, HORIZONTAL)
        ta.recycle()
    }

    private fun canScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> canScrollHorizontally(direction)
            1 -> canScrollVertically(direction)
            else -> throw IllegalArgumentException()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            handleInterceptTouchEvent(it)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setParentOrientation(orientation: Int) {
        parentOrientation = orientation
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        if (e.actionMasked == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
        } else if (e.actionMasked == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY

            val isParentHorizontal = parentOrientation == HORIZONTAL
            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            val scaledDx = dx.absoluteValue * if (isParentHorizontal) .5f else 1f
            val scaledDy = dy.absoluteValue * if (isParentHorizontal) 1f else .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isParentHorizontal == (scaledDy > scaledDx)) {
                    // Gesture is perpendicular, allow all parents to intercept
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    // Gesture is parallel, query child if movement in that direction is possible
                    if (canScroll(parentOrientation, if (isParentHorizontal) dx else dy)) {
                        // Child can scroll, disallow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        // Child cannot scroll, allow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        } else if (e.actionMasked == MotionEvent.ACTION_UP || e.actionMasked == MotionEvent.ACTION_CANCEL) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }
}