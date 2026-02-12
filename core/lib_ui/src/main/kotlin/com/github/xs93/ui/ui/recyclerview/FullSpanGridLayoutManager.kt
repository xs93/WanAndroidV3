package com.github.xs93.ui.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 处理了头布局和底部布局的SpanSize 问题的GridLayoutManager
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/3/5 14:29
 * @email 466911254@qq.com
 */
class FullSpanGridLayoutManager : GridLayoutManager {

    constructor(context: Context, spanCount: Int) : super(context, spanCount)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        context: Context,
        spanCount: Int,
        @RecyclerView.Orientation orientation: Int,
        reverseLayout: Boolean
    ) : super(context, spanCount, orientation, reverseLayout)

    private val fullSpanSizeLookup = FullSpanSizeLookup()

    private var adapter: RecyclerView.Adapter<*>? = null

    init {
        fullSpanSizeLookup.originalSpanSizeLookup = spanSizeLookup
        super.spanSizeLookup = fullSpanSizeLookup
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        adapter = newAdapter
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        adapter = view?.adapter
    }

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        super.onDetachedFromWindow(view, recycler)
        adapter = null
    }

    override fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup?) {
        fullSpanSizeLookup.originalSpanSizeLookup = spanSizeLookup
    }

    private inner class FullSpanSizeLookup : SpanSizeLookup() {
        var originalSpanSizeLookup: SpanSizeLookup? = null
        override fun getSpanSize(position: Int): Int {
            val adapter = adapter ?: return 1
            if (adapter is ConcatAdapter) {
                val pair = adapter.getWrappedAdapterAndPosition(position)

                return when (val wrappedAdapter = pair.first) {
                    is FullSpanAdapterType -> {
                        spanCount
                    }

                    is BaseAdapter<*, *> -> {
                        val type = wrappedAdapter.getItemViewType(pair.second)
                        if (wrappedAdapter.isFullSpanItem(type)) {
                            spanCount
                        } else {
                            originalSpanSizeLookup?.getSpanSize(position) ?: 1
                        }
                    }

                    else -> originalSpanSizeLookup?.getSpanSize(position) ?: 1
                }
            } else {
                return when (adapter) {
                    is FullSpanAdapterType -> {
                        spanCount
                    }

                    is BaseAdapter<*, *> -> {
                        val type = adapter.getItemViewType(position)
                        if (adapter.isFullSpanItem(type)) {
                            spanCount
                        } else {
                            originalSpanSizeLookup?.getSpanSize(position) ?: 1
                        }
                    }

                    else -> originalSpanSizeLookup?.getSpanSize(position) ?: 1
                }
            }
        }
    }
}