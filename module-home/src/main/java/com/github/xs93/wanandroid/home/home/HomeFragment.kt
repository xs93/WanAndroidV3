package com.github.xs93.wanandroid.home.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.xs93.core.base.ui.vbvm.BaseVbVmFragment
import com.github.xs93.core.ktx.dpToPx
import com.github.xs93.core.ktx.getColorCompat
import com.github.xs93.core.ktx.repeatOnLifecycle
import com.github.xs93.wanandroid.common.router.RouterPath
import com.github.xs93.wanandroid.home.R
import com.github.xs93.wanandroid.home.databinding.HomeFragHomeBinding
import com.github.xs93.wanandroid.home.model.Banner
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

/**
 * 主页
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/10/24 14:01
 * @email 466911254@qq.com
 */
@Route(path = RouterPath.Home.HomeFragment)
class HomeFragment : BaseVbVmFragment<HomeFragHomeBinding, HomeViewModel>(R.layout.home_frag_home) {

    private lateinit var mBannerAdapter: BannerAdapter

    @Suppress("UNCHECKED_CAST")
    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBannerAdapter = BannerAdapter()


        val bannerView: BannerViewPager<Banner> = binding.banner as BannerViewPager<Banner>
        bannerView.apply {
            setPageStyle(PageStyle.MULTI_PAGE_OVERLAP)
            setRevealWidth(requireContext().dpToPx(16))
            val normalColor = requireActivity().getColorCompat(com.github.xs93.core.R.color.white)
            val selectedColor = requireActivity().getColorCompat(com.github.xs93.wanandroid.common.R.color.primaryColor)
            setIndicatorSliderColor(normalColor, selectedColor)
            setIndicatorSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setIndicatorMargin(0, 0, 0, requireContext().dpToPx(24))
            setLifecycleRegistry(viewLifecycleOwner.lifecycle)
            setScrollDuration(500)
            setInterval(3000)
            adapter = mBannerAdapter
        }.create()
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.homeState.collect {
                binding.banner.refreshData(it.banners)
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        viewModel.input(HomeIntent.StartInitIntent)
    }
}