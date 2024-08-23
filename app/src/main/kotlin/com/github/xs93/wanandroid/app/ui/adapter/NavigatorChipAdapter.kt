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
class NavigatorChipAdapter : BaseQuickAdapter<Navigation, BaseViewBindingViewHolder<ItemNavigatorChipBinding>>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewBindingViewHolder<ItemNavigatorChipBinding> {
        return BaseViewBindingViewHolder(ItemNavigatorChipBinding.inflate(context.layoutInflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingViewHolder<ItemNavigatorChipBinding>,
        position: Int,
        item: Navigation?
    ) {

    }
}