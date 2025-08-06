package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.chad.library.adapter4.util.addOnDebouncedChildClick
import com.chad.library.adapter4.util.setOnDebouncedItemClick
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.observerState
import com.github.xs93.statuslayout.MultiStatusLayout
import com.github.xs93.utils.ktx.viewLifecycle
import com.github.xs93.utils.net.NetworkMonitor
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ExploreFragmentBinding
import com.github.xs93.wanandroid.app.ui.activity.ArticleWebActivity
import com.github.xs93.wanandroid.app.ui.adapter.ExploreBannerHeaderAdapter
import com.github.xs93.wanandroid.app.ui.adapter.HomeArticleAdapter
import com.github.xs93.wanandroid.app.ui.viewmodel.ExploreUiAction
import com.github.xs93.wanandroid.app.ui.viewmodel.ExploreViewModel
import com.github.xs93.wanandroid.common.model.CollectEvent
import com.github.xs93.wanandroid.common.model.ListUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

/**
 * 首页Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:23
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class ExploreFragment : BaseViewBindingFragment<ExploreFragmentBinding>(
    R.layout.explore_fragment,
    ExploreFragmentBinding::bind
) {
    companion object {
        fun newInstance(): ExploreFragment {
            val args = Bundle()
            val fragment = ExploreFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: ExploreViewModel by viewModels()

    private lateinit var bannerHeaderAdapter: ExploreBannerHeaderAdapter
    private lateinit var articleAdapter: HomeArticleAdapter
    private lateinit var adapterHelper: QuickAdapterHelper

    override fun initView(view: View, savedInstanceState: Bundle?) {
        bannerHeaderAdapter = ExploreBannerHeaderAdapter(viewLifecycle)
        articleAdapter = HomeArticleAdapter().apply {
            // 用于异步恢复状态
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            setOnDebouncedItemClick { _, _, position ->
                val article = items[position]
                ArticleWebActivity.start(requireContext(), article.link)
            }
            addOnDebouncedChildClick(R.id.img_collect) { adapter, _, position ->
                val article = adapter.getItem(position)
                article?.let {
                    viewModel.uiAction.send(
                        ExploreUiAction.CollectArticle(CollectEvent(it.id, it.collect.not()))
                    )
                }
            }
        }
        adapterHelper = QuickAdapterHelper.Builder(articleAdapter)
            .setTrailingLoadStateAdapter(object : TrailingLoadStateAdapter.OnTrailingListener {
                override fun onLoad() {
                    viewModel.uiAction.send(ExploreUiAction.RequestArticleData(false))
                }

                override fun onFailRetry() {
                    viewModel.uiAction.send(ExploreUiAction.RequestArticleData(false))
                }

                override fun isAllowLoading(): Boolean {
                    return !binding.refreshLayout.isRefreshing
                }
            })
            .build()
        adapterHelper.trailingLoadStateAdapter?.apply {
            isAutoLoadMore = true
            preloadSize = 4
        }
        adapterHelper.addBeforeAdapter(bannerHeaderAdapter)


        binding.apply {
            with(pageLayout) {
                setRetryClickListener {
                    viewModel.uiAction.send(ExploreUiAction.InitPageData)
                }
            }

            with(refreshLayout) {
                setOnRefreshListener {
                    viewModel.uiAction.send(ExploreUiAction.RequestArticleData(true))
                    adapterHelper.trailingLoadState = LoadState.None
                }
            }

            with(rvArticleList) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                // 移除动画,优化黑夜模式切换时，界面恢复导致的列表动画闪烁
                itemAnimator = null
                adapter = adapterHelper.adapter
            }
        }

        NetworkMonitor.observer(viewLifecycleOwner.lifecycle) { isConnected, _ ->
            if (binding.pageLayout.getViewStatus() == MultiStatusLayout.STATE_NO_NETWORK && isConnected) {
                viewModel.uiAction.send(ExploreUiAction.InitPageData)
            }
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)

        viewModel.registerCommonEvent(this)

        observerState(viewModel.uiStateFlow.map { it.pageStatus }) {
            binding.pageLayout.showViewByStatus(it.status)
        }

        observerState(viewModel.uiStateFlow.map { it.banners }) {
            bannerHeaderAdapter.item = it
        }

        observerState(viewModel.uiStateFlow.map { it.articlesListState }) {
            when (it.listUiState) {
                ListUiState.IDLE -> {}
                ListUiState.LoadMore -> {
                    if (binding.pageLayout.getViewStatus() == MultiStatusLayout.STATE_CONTENT) {
                        binding.refreshLayout.isRefreshing = false
                    }
                }

                ListUiState.Refreshing -> {
                    if (binding.pageLayout.getViewStatus() == MultiStatusLayout.STATE_CONTENT) {
                        binding.refreshLayout.isRefreshing = true
                    }
                }

                is ListUiState.LoadMoreFinished, is ListUiState.RefreshFinished -> {
                    articleAdapter.submitList(it.data) {
                        binding.refreshLayout.isRefreshing = false
                        adapterHelper.trailingLoadState = LoadState.NotLoading(it.noMoreData)
                    }
                }
            }
        }
    }

    override fun onFirstVisible() {
        viewModel.uiAction.send(ExploreUiAction.InitPageData)
    }
}