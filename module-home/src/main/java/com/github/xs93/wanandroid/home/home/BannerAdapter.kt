package com.github.xs93.wanandroid.home.home

import androidx.databinding.DataBindingUtil
import com.github.xs93.wanandroid.home.R
import com.github.xs93.wanandroid.home.databinding.HomeItemBannerBinding
import com.github.xs93.wanandroid.home.model.Banner
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
 * Banner适配器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 14:35
 * @email 466911254@qq.com
 */
class BannerAdapter : BaseBannerAdapter<Banner>() {
    override fun bindData(holder: BaseViewHolder<Banner>, data: Banner, position: Int, pageSize: Int) {
        val binding: HomeItemBannerBinding? = DataBindingUtil.bind(holder.itemView)
        binding?.banner = data
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.home_item_banner
    }
}