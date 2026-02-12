package com.github.xs93.ui.ui.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.github.xs93.ui.R
import java.util.Collections

/**
 * Adapter 封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/2/27 14:57
 * @email 466911254@qq.com
 */
abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder>(open var items: List<T> = emptyList()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _recyclerView: RecyclerView? = null

    val recyclerView: RecyclerView
        get() {
            checkNotNull(_recyclerView) {
                "Please get it after onAttachedToRecyclerView()"
            }
            return _recyclerView!!
        }

    val context: Context
        get() = recyclerView.context

    var isStateViewEnable: Boolean = false
        set(value) {
            val oldDisplayStateLayout = displayStateView()

            field = value

            val newDisplayStateLayout = displayStateView()

            if (oldDisplayStateLayout && !newDisplayStateLayout) {
                notifyItemRemoved(0)
            } else if (newDisplayStateLayout && !oldDisplayStateLayout) {
                notifyItemInserted(0)
            } else if (newDisplayStateLayout && oldDisplayStateLayout) {
                notifyItemChanged(0, PAYLOAD_STATE_VIEW)
            }
        }

    var stateView: View? = null
        set(value) {
            val oldDisplayStateLayout = displayStateView()

            field = value

            val newDisplayStateLayout = displayStateView()

            if (oldDisplayStateLayout && !newDisplayStateLayout) {
                notifyItemRemoved(0)
            } else if (newDisplayStateLayout && !oldDisplayStateLayout) {
                notifyItemInserted(0)
            } else if (newDisplayStateLayout && oldDisplayStateLayout) {
                notifyItemChanged(0, PAYLOAD_STATE_VIEW)
            }
        }

    /**
     * 是否使用StateView的大小
     */
    var isUseStateViewSize: Boolean = false

    protected open fun getItemViewType(position: Int, list: List<T>): Int = 0

    protected open fun getItemCount(items: List<T>): Int = items.size

    protected abstract fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VH

    protected abstract fun onBindViewHolder(holder: VH, position: Int, item: T?)

    protected open fun onBindViewHolder(holder: VH, position: Int, item: T?, payloads: List<Any>) {
        onBindViewHolder(holder, position, getItem(position))
    }

    final override fun getItemViewType(position: Int): Int {
        if (displayStateView()) return STATE_VIEW
        return getItemViewType(position, items)
    }


    final override fun getItemCount(): Int {
        if (displayStateView()) return 1
        return getItemCount(items)
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == STATE_VIEW) BaseAdapterStateLayoutVH(parent, stateView, isUseStateViewSize)
        return onCreateViewHolder(parent.context, parent, viewType)
    }

    @Suppress("UNCHECKED_CAST")
    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseAdapterStateLayoutVH) {
            holder.changeStateView(stateView, isUseStateViewSize)
            return
        }
        onBindViewHolder(holder as VH, position, getItem(position))
    }

    @Suppress("UNCHECKED_CAST")
    final override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }
        if (holder is BaseAdapterStateLayoutVH) {
            holder.changeStateView(stateView, isUseStateViewSize)
            return
        }
        onBindViewHolder(holder as VH, position, getItem(position), payloads)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        _recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _recyclerView = null
    }

    open fun isFullSpanItem(itemType: Int): Boolean {
        return itemType == STATE_VIEW
    }


    fun getItem(@IntRange(from = 0) position: Int): T? = items.getOrNull(position)

    /**
     * 获取对应首个匹配的 item 数据的索引。如果返回 -1，表示不存在
     * @param item T
     * @return Int
     */
    fun itemIndexOfFirst(item: T): Int {
        return items.indexOfFirst { item == it }
    }

    fun displayStateView(list: List<T> = items): Boolean {
        if (stateView == null || !isStateViewEnable) return false
        return list.isEmpty()
    }

    open fun submitList(list: List<T>?) {
        val newList = list ?: emptyList()
        val oldDisplayEmptyLayout = displayStateView()
        val newDisplayEmptyLayout = displayStateView(newList)

        if (oldDisplayEmptyLayout && !newDisplayEmptyLayout) {
            items = newList
            notifyItemRemoved(0)
            notifyItemRangeInserted(0, newList.size)
        } else if (newDisplayEmptyLayout && !oldDisplayEmptyLayout) {
            notifyItemRangeRemoved(0, items.size)
            items = newList
            notifyItemInserted(0)
        } else if (oldDisplayEmptyLayout && newDisplayEmptyLayout) {
            items = newList
            notifyItemChanged(0, PAYLOAD_STATE_VIEW)
        } else {
            items = newList
            notifyDataSetChanged()
        }
    }

    /**
     * change data
     * 改变某一位置数据
     */
    open operator fun set(@IntRange(from = 0) position: Int, data: T) {
        if (position >= items.size) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }
        mutableItems[position] = data
        notifyItemChanged(position)
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    open fun add(@IntRange(from = 0) position: Int, data: T) {
        if (position > items.size || position < 0) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (displayStateView()) {
            // 如果之前在显示空布局，需要先移除
            notifyItemRemoved(0)
        }
        mutableItems.add(position, data)
        notifyItemInserted(position)
    }

    /**
     * add one new data，not null.
     * 添加一条新数据，不可为 null。
     */
    open fun add(data: T) {
        if (displayStateView()) {
            // 如果之前在显示空布局，需要先移除
            notifyItemRemoved(0)
        }
        if (mutableItems.add(data)) {
            notifyItemInserted(items.size - 1)
        }
    }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param collection  the new data collection
     */
    open fun addAll(@IntRange(from = 0) position: Int, collection: Collection<T>) {
        if (collection.isEmpty()) return

        if (position > items.size || position < 0) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }

        if (displayStateView()) {
            // 如果之前在显示空布局，需要先移除
            notifyItemRemoved(0)
        }
        if (mutableItems.addAll(position, collection)) {
            notifyItemRangeInserted(position, collection.size)
        }
    }

    /**
     * 添加一组数据，不可为 null。
     */
    open fun addAll(collection: Collection<T>) {
        if (collection.isEmpty()) return

        if (displayStateView()) {
            // 如果之前在显示空布局，需要先移除
            notifyItemRemoved(0)
        }

        val oldSize = items.size
        if (mutableItems.addAll(collection)) {
            notifyItemRangeInserted(oldSize, collection.size)
        }
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    open fun removeAt(@IntRange(from = 0) position: Int) {
        if (position >= items.size) {
            throw IndexOutOfBoundsException("position: ${position}. size:${items.size}")
        }
        mutableItems.removeAt(position)
        notifyItemRemoved(position)

        // 处理空视图的情况
        if (displayStateView()) {
            notifyItemInserted(0)
        }
    }

    /**
     * 删除数据
     *
     * @param data
     */
    open fun remove(data: T) {
        val index = items.indexOf(data)
        if (index == -1) return
        removeAt(index)
    }

    /**
     * 删除给定范围内的数据
     *
     * @param range Int 索引范围
     */
    open fun removeAtRange(range: kotlin.ranges.IntRange) {
        if (range.isEmpty()) {
            return
        }
        if (range.first >= items.size) {
            throw IndexOutOfBoundsException("Range first position: ${range.first} - last position: ${range.last}. size:${items.size}")
        }

        val last = if (range.last >= items.size) {
            items.size - 1
        } else {
            range.last
        }

        for (it in last downTo range.first) {
            mutableItems.removeAt(it)
        }

        notifyItemRangeRemoved(range.first, last - range.first + 1)

        // 处理空视图的情况
        if (displayStateView()) {
            notifyItemInserted(0)
        }
    }

    /**
     * Item swap
     * 数据位置交换。这里单纯的只是两个数据交换位置。（注意⚠️，这里移动后的数据顺序与 [move] 不同)
     *
     * @param fromPosition
     * @param toPosition
     */
    open fun swap(fromPosition: Int, toPosition: Int) {
        if (fromPosition in items.indices && toPosition in items.indices) {
            Collections.swap(items, fromPosition, toPosition)
            notifyItemChanged(fromPosition)
            notifyItemChanged(toPosition)
        }
    }

    /**
     * Move Item
     * item 位置的移动。（注意⚠️，这里移动后的数据顺序与 [swap] 不同)
     *
     * @param fromPosition
     * @param toPosition
     */
    open fun move(fromPosition: Int, toPosition: Int) {
        if (fromPosition in items.indices && toPosition in items.indices) {
            val e = mutableItems.removeAt(fromPosition)
            mutableItems.add(toPosition, e)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    /**
     * items 转化为 MutableList
     */
    private val mutableItems: MutableList<T>
        get() {
            return when (items) {
                is ArrayList -> {
                    items as ArrayList
                }

                is MutableList -> {
                    items as MutableList
                }

                else -> {
                    items.toMutableList().apply { items = this }
                }
            }
        }

    companion object {
        val STATE_VIEW = R.id.baseAdapter_state_view
        internal const val PAYLOAD_STATE_VIEW = 0
    }
}