package com.github.xs93.framework.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 线性列表Item 空白间距
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/6/19 14:06
 * @email 466911254@qq.com
 */
class LinearSpacingItemDecoration(private val spacingWidth: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager
        if (layoutManager !is LinearLayoutManager) {
            return
        }
        val itemPosition = parent.getChildAdapterPosition(view)
        val orientation = layoutManager.orientation
        if (orientation == GridLayoutManager.VERTICAL) {
            if (itemPosition > 0) {
                outRect.top = spacingWidth
            }
        } else {
            if (itemPosition > 0) {
                outRect.left = spacingWidth
            }
        }
    }
}