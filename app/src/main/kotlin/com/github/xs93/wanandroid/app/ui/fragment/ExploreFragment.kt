package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.util.addOnDebouncedChildClick
import com.chad.library.adapter4.util.setOnDebouncedItemClick
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.observerState
import com.github.xs93.statuslayout.MultiStatusLayout
import com.github.xs93.utils.ktx.getParcelableCompat
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
class ExploreFragment :
    BaseViewBindingFragment<ExploreFragmentBinding>(R.layout.explore_fragment, ExploreFragmentBinding::bind) {
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
    private lateinit var mLayoutManager: LinearLayoutManager

    private var layoutManagerState: Parcelable? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
        bannerHeaderAdapter = ExploreBannerHeaderAdapter(viewLifecycle)
        articleAdapter = HomeArticleAdapter().apply {
            setOnDebouncedItemClick { _, _, position ->
                val article = items[position]
                ArticleWebActivity.start(requireContext(), article.link)
            }
            addOnDebouncedChildClick(R.id.img_collect) { adapter, _, position ->
                val article = adapter.getItem(position)
                article?.let {
                    viewModel.uiAction.send(ExploreUiAction.CollectArticle(CollectEvent(it.id, it.collect.not())))
                }
            }
        }

        adapterHelper = QuickAdapterHelper.Builder(articleAdapter).build().addBeforeAdapter(bannerHeaderAdapter)

        binding.apply {
            with(pageLayout) {
                setRetryClickListener {
                    viewModel.uiAction.send(ExploreUiAction.InitPageData)
                }
            }

            with(refreshLayout) {
                setOnRefreshListener {
                    viewModel.uiAction.send(ExploreUiAction.RequestArticleData(true))
                }

                setOnLoadMoreListener {
                    viewModel.uiAction.send(ExploreUiAction.RequestArticleData(false))
                }
            }

            with(rvArticleList) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    .also { mLayoutManager = it }
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
            when (val uiState = it.listUiState) {
                ListUiState.IDLE -> {}
                ListUiState.LoadMore -> {
                    if (binding.pageLayout.getViewStatus() == MultiStatusLayout.STATE_CONTENT) {
                        binding.refreshLayout.autoLoadMoreAnimationOnly()
                    }
                }

                ListUiState.Refreshing -> {
                    if (binding.pageLayout.getViewStatus() == MultiStatusLayout.STATE_CONTENT) {
                        binding.refreshLayout.autoRefreshAnimationOnly()
                    }
                }

                is ListUiState.LoadMoreFinished, is ListUiState.RefreshFinished -> {
                    articleAdapter.submitList(it.data) {
                        if (uiState is ListUiState.RefreshFinished) {
                            binding.refreshLayout.finishRefresh(uiState.success)
                            binding.refreshLayout.setNoMoreData(it.noMoreData)
                        }
                        if (uiState is ListUiState.LoadMoreFinished) {
                            binding.refreshLayout.finishLoadMore(uiState.success)
                            binding.refreshLayout.setNoMoreData(it.noMoreData)
                        }
                        // 从重建中恢复滚动状态
                        if (layoutManagerState != null) {
                            mLayoutManager.onRestoreInstanceState(layoutManagerState)
                            layoutManagerState = null
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("articleLayoutManagerState", mLayoutManager.onSaveInstanceState())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val state = savedInstanceState?.getParcelableCompat("articleLayoutManagerState", Parcelable::class.java)
        layoutManagerState = state
    }

    override fun onFirstVisible() {
        viewModel.uiAction.send(ExploreUiAction.InitPageData)
    }
}