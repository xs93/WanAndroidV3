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
import com.github.xs93.framework.base.ui.viewbinding.BaseVBFragment
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.observerState
import com.github.xs93.statuslayout.MultiStatusLayout
import com.github.xs93.utils.net.NetworkMonitor
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.SquareFragmentBinding
import com.github.xs93.wanandroid.app.ui.activity.ArticleWebActivity
import com.github.xs93.wanandroid.app.ui.adapter.HomeArticleAdapter
import com.github.xs93.wanandroid.app.ui.viewmodel.SquareUiAction
import com.github.xs93.wanandroid.app.ui.viewmodel.SquareViewModel
import com.github.xs93.wanandroid.common.model.CollectEvent
import com.github.xs93.wanandroid.common.model.ListUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 14:30
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class SquareFragment : BaseVBFragment<SquareFragmentBinding>(
    SquareFragmentBinding::inflate
) {

    companion object {

        fun newInstance(): SquareFragment {
            val args = Bundle()
            val fragment = SquareFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: SquareViewModel by viewModels()

    private var articleAdapter: HomeArticleAdapter? = null
    private var quickAdapterHelper: QuickAdapterHelper? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
        viewBinding.apply {
            with(pageLayout) {
                setRetryClickListener {
                    viewModel.uiAction.send(SquareUiAction.InitPageData)
                }
            }

            with(refreshLayout) {
                setOnRefreshListener {
                    viewModel.uiAction.send(SquareUiAction.RequestArticleData(true))
                }
            }

            with(rvArticleList) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                val articleAdapter = HomeArticleAdapter().apply {
                    // 用于异步恢复状态
                    stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                    setOnDebouncedItemClick { _, _, position ->
                        val article = items[position]
                        ArticleWebActivity.start(requireContext(), article.link)
                    }
                    addOnDebouncedChildClick(R.id.img_collect) { adapter, _, position ->
                        val article = adapter.getItem(position)
                        article?.let {
                            viewModel.uiAction.send(
                                SquareUiAction.CollectArticle(
                                    CollectEvent(
                                        it.id,
                                        it.collect.not()
                                    )
                                )
                            )
                        }
                    }
                }.also { articleAdapter = it }


                val adapterHelper = QuickAdapterHelper.Builder(articleAdapter)
                    .setTrailingLoadStateAdapter(object :
                        TrailingLoadStateAdapter.OnTrailingListener {
                        override fun onLoad() {
                            viewModel.uiAction.send(SquareUiAction.RequestArticleData(false))
                        }

                        override fun onFailRetry() {
                            viewModel.uiAction.send(SquareUiAction.RequestArticleData(false))
                        }

                        override fun isAllowLoading(): Boolean {
                            return !viewBinding.refreshLayout.isRefreshing
                        }
                    })
                    .build()
                    .also { quickAdapterHelper = it }
                adapterHelper.trailingLoadStateAdapter?.apply {
                    isAutoLoadMore = true
                    preloadSize = 4
                }
                adapter = adapterHelper.adapter
            }
        }

        NetworkMonitor.observer(viewLifecycleOwner.lifecycle) { isConnected, _ ->
            if (viewBinding.pageLayout.getViewStatus() == MultiStatusLayout.STATE_NO_NETWORK && isConnected) {
                viewModel.uiAction.send(SquareUiAction.InitPageData)
            }
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)

        viewModel.registerCommonEvent(this)

        observerState(viewModel.uiStateFlow.map { it.pageStatus }) {
            viewBinding.pageLayout.showViewByStatus(it.status)
        }

        observerState(viewModel.uiStateFlow.map { it.articlesListState }) {
            when (val uiState = it.listUiState) {
                ListUiState.IDLE -> {}
                is ListUiState.RequestStart -> {
                    viewBinding.refreshLayout.isRefreshing = uiState.refreshing
                }

                is ListUiState.RequestFinish -> {
                    viewBinding.refreshLayout.isRefreshing = false
                    articleAdapter?.submitList(it.data) {
                        quickAdapterHelper?.trailingLoadState =
                            LoadState.NotLoading(uiState.noMoreData)
                    }
                }

                is ListUiState.RequestFinishFailed -> {
                    viewBinding.refreshLayout.isRefreshing = false
                    quickAdapterHelper?.trailingLoadState = LoadState.Error(uiState.error)
                }
            }
        }
    }

    override fun onFirstVisible() {
        viewModel.uiAction.send(SquareUiAction.InitPageData)
    }
}