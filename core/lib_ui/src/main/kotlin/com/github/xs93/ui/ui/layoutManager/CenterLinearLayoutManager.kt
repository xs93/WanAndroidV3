package com.github.xs93.ui.ui.layoutManager

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * 选中居中的LayoutManager
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/15 11:29
 * @email 466911254@qq.com
 */
class CenterLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(context, orientation, reverseLayout) {


    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
        val smoothScroller = CenterSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {

        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int,
        ): Int {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 100f / displayMetrics.densityDpi
        }
    }
}