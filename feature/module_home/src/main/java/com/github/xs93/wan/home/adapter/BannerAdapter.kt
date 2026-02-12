package com.github.xs93.wan.home.adapter

import android.view.View
import android.view.ViewGroup
import coil3.load
import com.github.xs93.wan.data.entity.Banner
import com.github.xs93.wan.home.R
import com.github.xs93.wan.home.databinding.HomeItemExploreBannerBinding
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
 * 首页Banner适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/23 10:21
 * @email 466911254@qq.com
 */
class BannerAdapter : BaseBannerAdapter<Banner>() {

    override fun createViewHolder(
        parent: ViewGroup,
        itemView: View,
        viewType: Int
    ): BaseViewHolder<Banner> {
        val binding = HomeItemExploreBannerBinding.bind(itemView)
        return ViewBindingViewHolder(binding)
    }

    override fun bindData(
        holder: BaseViewHolder<Banner>?,
        data: Banner?,
        position: Int,
        pageSize: Int
    ) {
        if (holder is ViewBindingViewHolder) {
            with(holder.binding) {
                imgContent.load(data?.imagePath)
                txtTitle.text = data?.title
            }
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.home_item_explore_banner
    }

    internal class ViewBindingViewHolder(val binding: HomeItemExploreBannerBinding) :
        BaseViewHolder<Banner>(binding.root)
}