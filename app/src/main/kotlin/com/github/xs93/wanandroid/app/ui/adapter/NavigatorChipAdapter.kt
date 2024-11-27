package com.github.xs93.wanandroid.app.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.github.xs93.utils.ktx.layoutInflater
import com.github.xs93.wanandroid.app.databinding.ItemNavigatorChipBinding
import com.github.xs93.wanandroid.common.entity.Navigation
import com.github.xs93.wanandroid.common.ui.viewbinding.BaseViewBindingViewHolder

/**
 * 导航chipAdapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/8 10:57
 * @email 466911254@qq.com
 */
class NavigatorChipAdapter :
    BaseQuickAdapter<Navigation, BaseViewBindingViewHolder<ItemNavigatorChipBinding>>() {

    companion object {
        private const val SELECT_STATE_CHANGED = "select_state_changed"
    }

    private var selectedNavigationCid: Int = -1

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewBindingViewHolder<ItemNavigatorChipBinding> {
        return BaseViewBindingViewHolder(
            ItemNavigatorChipBinding.inflate(context.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingViewHolder<ItemNavigatorChipBinding>,
        position: Int,
        item: Navigation?
    ) {
        holder.binding.apply {
            tvChipName.text = item?.name
            tvChipName.isSelected = selectedNavigationCid == item?.cid
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingViewHolder<ItemNavigatorChipBinding>,
        position: Int,
        item: Navigation?,
        payloads: List<Any>
    ) {
        if (payloads.contains(SELECT_STATE_CHANGED)) {
            holder.binding.tvChipName.isSelected = selectedNavigationCid == item?.cid
            return
        }
        super.onBindViewHolder(holder, position, item, payloads)
    }

    fun setSelectedNavigationCid(cid: Int) {
        selectedNavigationCid = cid
        notifyItemRangeChanged(0, itemCount, SELECT_STATE_CHANGED)
    }

    fun setSelectedNavigation(navigation: Navigation) {
        setSelectedNavigationCid(navigation.cid)
    }
}