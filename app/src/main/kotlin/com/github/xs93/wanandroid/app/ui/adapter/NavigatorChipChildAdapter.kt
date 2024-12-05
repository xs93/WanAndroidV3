package com.github.xs93.wanandroid.app.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.github.xs93.utils.ktx.layoutInflater
import com.github.xs93.utils.ktx.toHtml
import com.github.xs93.wanandroid.app.databinding.ItemNavigationChipChildBinding
import com.github.xs93.wanandroid.common.entity.Article
import com.github.xs93.wanandroid.common.ui.viewbinding.BaseViewBindingViewHolder

/**
 * chip 列表适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/12/2 9:19
 * @email 466911254@qq.com
 */
class NavigatorChipChildAdapter :
    BaseQuickAdapter<Article, BaseViewBindingViewHolder<ItemNavigationChipChildBinding>>() {
    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewBindingViewHolder<ItemNavigationChipChildBinding> {
        val binding = ItemNavigationChipChildBinding.inflate(context.layoutInflater, parent, false)
        return BaseViewBindingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingViewHolder<ItemNavigationChipChildBinding>,
        position: Int,
        item: Article?,
    ) {
        item?.let {
            holder.binding.tvChipChild.text = it.title.toHtml()
        }
    }
}