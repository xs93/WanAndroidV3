package com.github.xs93.ui.ui.recyclerview

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

/**
 * BaseAdapter 状态布局ViewHolder
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/3/5 13:23
 * @email 466911254@qq.com
 */
internal class BaseAdapterStateLayoutVH(
    parent: ViewGroup,
    stateView: View?,
    isUseStateViewSize: Boolean,
    private val stateLayout: FrameLayout = FrameLayout(parent.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setStateView(this, stateView, isUseStateViewSize)
    }
) : RecyclerView.ViewHolder(stateLayout) {

    fun changeStateView(stateView: View?, isUseStateViewSize: Boolean) {
        setStateView(stateLayout, stateView, isUseStateViewSize)
    }

    companion object {
        private fun setStateView(
            rootView: ViewGroup,
            stateView: View?,
            isUseStateViewSize: Boolean,
        ) {
            if (stateView == null) {
                rootView.removeAllViews()
                return
            }

            if (rootView.childCount == 1) {
                val old = rootView.getChildAt(0)
                if (old == stateView) {
                    // 如果是同一个view，不进行操作
                    return
                }
            }

            stateView.parent.run {
                if (this is ViewGroup) {
                    this.removeView(stateView)
                }
            }

            if (stateView.layoutParams == null) {
                stateView.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                }
            }

            if (isUseStateViewSize) {
                val lp = rootView.layoutParams
                lp.height = stateView.layoutParams.height
                lp.width = stateView.layoutParams.width
                rootView.layoutParams = lp
            }

            rootView.removeAllViews()
            rootView.addView(stateView)
        }
    }

}