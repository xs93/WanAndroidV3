package com.github.xs93.wanandroid.home.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.github.xs93.core.base.ui.vbvm.BaseVbVmFragment
import com.github.xs93.core.ktx.dp
import com.github.xs93.core.ktx.getColorCompat
import com.github.xs93.core.ktx.repeatOnLifecycle
import com.github.xs93.core.utils.toast.ToastUtils
import com.github.xs93.wanandroid.common.router.RouterPath
import com.github.xs93.wanandroid.common.rv.ArticleDiffCallback
import com.github.xs93.wanandroid.home.R
import com.github.xs93.wanandroid.home.databinding.HomeFragHomeBinding
import com.github.xs93.wanandroid.home.databinding.HomeHeaderBannerBinding
import com.github.xs93.wanandroid.home.model.Banner
import com.scwang.smart.refresh.header.MaterialHeader
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.IndicatorGravity
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

    private val articleAdapter by lazy {
        ArticleAdapter()
    }

    private val bannerAdapter by lazy {
        BannerAdapter()
    }
    private val mHomeHeaderBannerBinding: HomeHeaderBannerBinding by lazy {
        HomeHeaderBannerBinding.inflate(LayoutInflater.from(context), null, false)
    }

    @Suppress("UNCHECKED_CAST")
    override fun initView(view: View, savedInstanceState: Bundle?) {
        mHomeHeaderBannerBinding.apply {
            val bannerView: BannerViewPager<Banner> = banner as BannerViewPager<Banner>
            bannerView.apply {
                val normalColor = context.getColorCompat(com.github.xs93.core.R.color.white)
                val selectedColor = context.getColorCompat(com.github.xs93.wanandroid.common.R.color.primaryColor)
                setIndicatorSliderColor(normalColor, selectedColor)
                setIndicatorSlideMode(IndicatorSlideMode.WORM)
                setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                setIndicatorMargin(0, 0, 12.dp(context), 16.dp(context))
                setIndicatorGravity(IndicatorGravity.END)
                setLifecycleRegistry(viewLifecycleOwner.lifecycle)
                setScrollDuration(500)
                setInterval(3000)
                adapter = bannerAdapter
                setOnPageClickListener { _, position ->
                    val banner = data[position]
                    ARouter.getInstance().build(RouterPath.Web.WebActivity)
                        .withString("title", banner.title)
                        .withString("url", banner.url)
                        .navigation()
                }
            }.create()
        }

        articleAdapter.apply {
            addHeaderView(mHomeHeaderBannerBinding.root)
            setDiffCallback(ArticleDiffCallback())
            setOnItemClickListener { _, _, position ->
                val article = articleAdapter.data[position]
                ARouter.getInstance().build(RouterPath.Web.WebActivity)
                    .withString("title", article.title)
                    .withString("url", article.link)
                    .navigation()
            }
            setOnItemChildClickListener { _, _, position ->
                val article = articleAdapter.data[position]
                ToastUtils.show("收藏")
            }
        }

        binding.rvArticle.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
            addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)
                    if (position > 0) {
                        outRect.set(0, 4.dp(context), 0, 0)
                    }
                }
            })
        }

        binding.srl.apply {
            setEnableAutoLoadMore(true)
            refreshHeader?.apply {
                if (this is MaterialHeader) {
                    setColorSchemeResources(com.github.xs93.wanandroid.common.R.color.primaryColor)
                }
            }
            setOnRefreshListener {
                viewModel.input(HomeIntent.StartInitIntent)
            }
            setOnLoadMoreListener {
                viewModel.input(HomeIntent.LoadMoreArticle)
            }
        }

    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.homeState.collect {
                if (mHomeHeaderBannerBinding.banner.data.isEmpty()) {
                    mHomeHeaderBannerBinding.banner.refreshData(it.banners)
                }
                articleAdapter.setDiffNewData(it.articles.toMutableList())
            }
        }

        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.homeEvent.collect {
                when (it) {
                    HomeEvent.StartRefreshEvent -> {
                        binding.srl.autoRefresh()
                    }
                    is HomeEvent.FinishRefreshEvent -> {
                        binding.srl.finishRefresh(it.success)
                    }

                    is HomeEvent.FinishLoadMoreEvent -> {
                        binding.srl.finishLoadMore(it.success)
                    }
                    HomeEvent.StartLoadMoreEvent -> {
                        binding.srl.autoLoadMore()
                    }
                }
            }
        }

    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        binding.srl.autoRefresh()
    }
}