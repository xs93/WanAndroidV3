package com.github.xs93.ui.kts

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field

/**
 * ViewPager2 相关功能扩展
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/4 13:21
 * @email 466911254@qq.com
 */

/**
 * 修改灵敏度,修改滑动触发的距离倍数
 * @receiver ViewPager2
 * @param multiple Int
 */
fun ViewPager2.setTouchSlopMultiple(multiple: Float) {
    try {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView: RecyclerView = recyclerViewField.get(this) as RecyclerView
        val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, (touchSlop * multiple).toInt())
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 通过反射修改ViewPager2 滚动切换动画时间长度
 * @receiver ViewPager2
 * @param duration Long
 */
fun ViewPager2.setSmoothScrollDuration(duration: Long) {
    try {
        val recyclerView: RecyclerView = getChildAt(0) as RecyclerView
        recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val linearLayoutManager: LinearLayoutManager =
            recyclerView.layoutManager as LinearLayoutManager? ?: return

        val scrollDurationManager = ScrollDurationManager(this, duration, linearLayoutManager)
        recyclerView.layoutManager = scrollDurationManager

        val mRecyclerViewField =
            RecyclerView.LayoutManager::class.java.getDeclaredField("mRecyclerView")
        mRecyclerViewField.isAccessible = true
        mRecyclerViewField.set(linearLayoutManager, recyclerView)

        val layoutManagerField = ViewPager2::class.java.getDeclaredField("mLayoutManager")
        layoutManagerField.isAccessible = true
        layoutManagerField.set(this, scrollDurationManager)

        val pageTransformerAdapterField =
            ViewPager2::class.java.getDeclaredField("mPageTransformerAdapter")
        pageTransformerAdapterField.isAccessible = true

        val mPageTransformerAdapter = pageTransformerAdapterField.get(this)
        if (mPageTransformerAdapter != null) {
            val aClass = mPageTransformerAdapter.javaClass
            val layoutManager = aClass.getDeclaredField("mLayoutManager")
            layoutManager.isAccessible = true
            layoutManager.set(mPageTransformerAdapter, scrollDurationManager)
        }

        val scrollEventAdapterField = ViewPager2::class.java.getDeclaredField("mScrollEventAdapter")
        scrollEventAdapterField.isAccessible = true
        val mScrollEventAdapter = scrollEventAdapterField.get(this)
        if (mScrollEventAdapter != null) {
            val aClass = mScrollEventAdapter.javaClass
            val layoutManager = aClass.getDeclaredField("mLayoutManager")
            layoutManager.isAccessible = true
            layoutManager.set(mScrollEventAdapter, scrollDurationManager)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private class ScrollDurationManager(
    viewPager2: ViewPager2,
    private val scrollDuration: Long,
    private val layoutManager: LinearLayoutManager
) : LinearLayoutManager(
    viewPager2.context,
    layoutManager.orientation,
    false
) {
    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
        val linearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {
            override fun calculateTimeForDeceleration(dx: Int): Int {
                return scrollDuration.toInt()
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    override fun performAccessibilityAction(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        action: Int,
        args: Bundle?
    ): Boolean {
        return layoutManager.performAccessibilityAction(recycler, state, action, args)
    }

    override fun onInitializeAccessibilityNodeInfo(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        info: AccessibilityNodeInfoCompat
    ) {
        layoutManager.onInitializeAccessibilityNodeInfo(recycler, state, info)
    }

    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        try {

            val method = layoutManager::class.java.getDeclaredMethod(
                "calculateExtraLayoutSpace",
                state::class.java,
                extraLayoutSpace::class.java
            )
            method.isAccessible = true
            method.invoke(layoutManager, state, extraLayoutSpace)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean
    ): Boolean {
        return false // users should use setCurrentItem instead
    }
}