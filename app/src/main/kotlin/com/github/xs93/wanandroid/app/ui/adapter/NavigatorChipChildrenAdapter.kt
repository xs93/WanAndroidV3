package com.github.xs93.wanandroid.app.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.github.xs93.utils.ktx.layoutInflater
import com.github.xs93.wan.common.entity.Navigation
import com.github.xs93.wan.common.ui.viewbinding.BaseVBViewHolder
import com.github.xs93.wanandroid.app.databinding.ItemNavigatorChipChildrenBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/**
 * 导航子项适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/29 9:35
 * @email 466911254@qq.com
 */
class NavigatorChipChildrenAdapter :
    BaseQuickAdapter<Navigation, BaseVBViewHolder<ItemNavigatorChipChildrenBinding>>() {
    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): BaseVBViewHolder<ItemNavigatorChipChildrenBinding> {
        val binding =
            ItemNavigatorChipChildrenBinding.inflate(context.layoutInflater, parent, false)
        // 解决嵌套滚动问题
        binding.rvChipChildrenList.isNestedScrollingEnabled = false
        val flexboxLayoutManager = FlexboxLayoutManager(context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            alignItems = AlignItems.FLEX_START
        }
        binding.rvChipChildrenList.layoutManager = flexboxLayoutManager
        binding.rvChipChildrenList.adapter = NavigatorChipChildAdapter()
        return BaseVBViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BaseVBViewHolder<ItemNavigatorChipChildrenBinding>,
        position: Int,
        item: Navigation?,
    ) {
        item?.let {
            holder.binding.tvChipName.text = it.name
            val adapter = holder.binding.rvChipChildrenList.adapter as? NavigatorChipChildAdapter
            adapter?.submitList(it.articles)
        }
    }
}