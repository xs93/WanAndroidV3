package com.github.xs93.framework.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlin.math.roundToInt

/**
 *  Guide 添加空白分割线,
 *  @param horizontalSpaceWidth   column 之间的间隙
 *  @param verticalSpaceWidth  Row之间的的间隙
 * @author XuShuai
 * @version v1.0
 * @date 2023/3/24 14:29
 * @email 466911254@qq.com
 */


class GridSpacingItemDecoration(
    private val horizontalSpaceWidth: Int,
    private val verticalSpaceWidth: Int
) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager = parent.layoutManager
        if (layoutManager !is GridLayoutManager) {
            return
        }

        val spanCount = layoutManager.spanCount
        if (spanCount == 0) {
            return
        }

        val itemPosition = parent.getChildAdapterPosition(view)
        val orientation = layoutManager.orientation

        if (orientation == GridLayoutManager.VERTICAL) {
            val column = itemPosition % spanCount
            outRect.left = (column * horizontalSpaceWidth * 1.0f / spanCount).roundToInt()
            outRect.right = (horizontalSpaceWidth - (column + 1) * horizontalSpaceWidth * 1.0f / spanCount).roundToInt()
            if (itemPosition / spanCount == 0) {
                outRect.top = 0
            } else {
                outRect.top = verticalSpaceWidth
            }
        } else {
            val row = itemPosition % spanCount
            outRect.top = (row * verticalSpaceWidth * 1.0f / spanCount).roundToInt()
            outRect.bottom = (verticalSpaceWidth - (row + 1) * verticalSpaceWidth * 1.0f / spanCount).roundToInt()
            if (itemPosition / spanCount == 0) {
                outRect.left = 0
            } else {
                outRect.left = horizontalSpaceWidth
            }
        }
    }
}