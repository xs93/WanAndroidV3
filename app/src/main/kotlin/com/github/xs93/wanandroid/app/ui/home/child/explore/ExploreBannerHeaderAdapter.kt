package com.github.xs93.wanandroid.app.ui.home.child.explore

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseSingleItemAdapter
import com.github.xs93.utils.ktx.color
import com.github.xs93.utils.ktx.dp
import com.github.xs93.utils.ktx.getColorByAttr
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ExploreBannerLayoutBinding
import com.github.xs93.wanandroid.common.entity.Banner
import com.github.xs93.wanandroid.web.WebActivity
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

/**
 * BannerHeader头部View信息
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 15:30
 * @email 466911254@qq.com
 */
class ExploreBannerHeaderAdapter(private val lifecycle: Lifecycle) :
    BaseSingleItemAdapter<List<Banner>, ExploreBannerHeaderAdapter.BannerHeaderViewHolder>() {

    class BannerHeaderViewHolder(val bind: ExploreBannerLayoutBinding) :
        RecyclerView.ViewHolder(bind.root)

    override fun onBindViewHolder(holder: BannerHeaderViewHolder, item: List<Banner>?) {
        holder.bind.banner.refreshData(item)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): BannerHeaderViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ExploreBannerLayoutBinding.inflate(layoutInflater, parent, false)
        val bannerViewPager: BannerViewPager<Banner> = binding.root.findViewById(R.id.banner)
        bannerViewPager.apply {
            val normalColor = context.color(com.github.xs93.framework.R.color.color_666666)
            val selectedColor = context.getColorByAttr(androidx.appcompat.R.attr.colorPrimary)
            setIndicatorSliderColor(normalColor, selectedColor)
            setIndicatorSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setIndicatorMargin(0, 0, 12.dp(context).toInt(), 16.dp(context).toInt())
            setIndicatorGravity(IndicatorGravity.CENTER)
            setScrollDuration(500)
            registerLifecycleObserver(lifecycle)
            setOffScreenPageLimit(2)
            disallowParentInterceptDownEvent(true)
            setPageStyle(PageStyle.NORMAL, 1.0f)
            setInterval(3000)
            adapter = BannerAdapter()
            setOnPageClickListener { _, position ->
                val banner = data[position]
                WebActivity.start(context, banner.url, banner.title)
            }
        }.create()
        return BannerHeaderViewHolder(binding)
    }
}