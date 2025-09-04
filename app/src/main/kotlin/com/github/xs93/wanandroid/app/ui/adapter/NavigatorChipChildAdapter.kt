package com.github.xs93.wanandroid.app.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.github.xs93.utils.ktx.layoutInflater
import com.github.xs93.utils.ktx.toHtml
import com.github.xs93.wan.common.entity.Article
import com.github.xs93.wan.common.ui.viewbinding.BaseVBViewHolder
import com.github.xs93.wanandroid.app.databinding.ItemNavigationChipChildBinding

/**
 * chip 列表适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/12/2 9:19
 * @email 466911254@qq.com
 */
class NavigatorChipChildAdapter :
    BaseQuickAdapter<Article, BaseVBViewHolder<ItemNavigationChipChildBinding>>() {
    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): BaseVBViewHolder<ItemNavigationChipChildBinding> {
        val binding = ItemNavigationChipChildBinding.inflate(context.layoutInflater, parent, false)
        return BaseVBViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BaseVBViewHolder<ItemNavigationChipChildBinding>,
        position: Int,
        item: Article?,
    ) {
        item?.let {
            holder.binding.tvChipChild.text = it.title.toHtml()
        }
    }
}