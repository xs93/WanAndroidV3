package com.github.xs93.wanandroid.app.ui.home

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.core.base.ui.vbvm.BaseVbVmFragment
import com.github.xs93.framework.core.ktx.dp
import com.github.xs93.framework.core.ktx.getColorCompat
import com.github.xs93.framework.core.ktx.observer
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentHomeBinding
import com.github.xs93.wanandroid.app.entity.Banner
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.coroutines.flow.map

/**
 * 首页Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:23
 * @email 466911254@qq.com
 */
class HomeFragment : BaseVbVmFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var bannerViewPager: BannerViewPager<Banner>
    private val bannerAdapter: BannerAdapter = BannerAdapter()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        bannerViewPager = view.findViewById(R.id.banner)
        bannerViewPager.apply {
            val normalColor = context.getColorCompat(com.github.xs93.framework.R.color.color_666666)
            val selectedColor = context.getColorCompat(com.github.xs93.common.R.color.primaryColor)
            setIndicatorSliderColor(normalColor, selectedColor)
            setIndicatorSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setIndicatorMargin(0, 0, 12.dp(context), 16.dp(context))
            setIndicatorGravity(IndicatorGravity.CENTER)
            registerLifecycleObserver(lifecycle)
            setScrollDuration(500)
            setOffScreenPageLimit(2)
            disallowParentInterceptDownEvent(true)
            setPageStyle(PageStyle.NORMAL, 1.0f)
            setInterval(3000)
            adapter = bannerAdapter
            setOnPageClickListener { _, position ->
                showToast("$position")
            }
        }.create()
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        observer(viewModel.uiStateFlow.map { it.banners }) {
            bannerViewPager.refreshData(it)
        }
    }

    override fun onFirstVisible() {
        viewModel.sendUiIntent(HomeUiIntent.InitBannerData)
    }
}