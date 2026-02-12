package com.github.xs93.ui.ui.recyclerview

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.xs93.ui.ui.recyclerview.HeaderOrFooterAdapter.HeaderViewHolder

/**
 * 封装的头布局Adapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/2/27 15:20
 * @email 466911254@qq.com
 */
class HeaderOrFooterAdapter(val headerView: View) : RecyclerView.Adapter<HeaderViewHolder>(),
    FullSpanAdapterType {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder =
        HeaderViewHolder(parent, headerView)

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {}
    override fun getItemCount(): Int = 1

    class HeaderViewHolder(
        parent: ViewGroup,
        itemView: View,
        containerLayout: FrameLayout = FrameLayout(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            itemView.parent?.run {
                if (this is ViewGroup) {
                    this.removeView(itemView)
                }
            }
            if (itemView.layoutParams == null) {
                itemView.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                }
            }
            removeAllViews()
            addView(itemView)
        }
    ) : RecyclerView.ViewHolder(containerLayout)
}