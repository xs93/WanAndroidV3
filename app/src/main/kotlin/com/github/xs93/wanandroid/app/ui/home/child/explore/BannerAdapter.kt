package com.github.xs93.wanandroid.app.ui.home.child.explore

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ExploreBannerItemBinding
import com.github.xs93.wanandroid.app.entity.Banner
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
        val binding = DataBindingUtil.bind<ExploreBannerItemBinding>(itemView)
            ?: throw NullPointerException("binding is Null")
        return DataBindingViewHolder(binding)
    }

    override fun bindData(
        holder: BaseViewHolder<Banner>?,
        data: Banner?,
        position: Int,
        pageSize: Int
    ) {
        if (holder is DataBindingViewHolder) {
            holder.binding.banner = data
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.explore_banner_item
    }

    internal class DataBindingViewHolder(val binding: ExploreBannerItemBinding) :
        BaseViewHolder<Banner>(binding.root)
}