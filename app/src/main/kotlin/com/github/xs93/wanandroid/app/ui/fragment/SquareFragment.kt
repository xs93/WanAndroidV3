package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.util.addOnDebouncedChildClick
import com.chad.library.adapter4.util.setOnDebouncedItemClick
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
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
class SquareFragment : BaseViewBindingFragment<SquareFragmentBinding>(
    R.layout.square_fragment,
    SquareFragmentBinding::bind
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

    private lateinit var articleAdapter: HomeArticleAdapter

    override fun initView(view: View, savedInstanceState: Bundle?) {
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
                    viewModel.uiAction.send(SquareUiAction.CollectArticle(CollectEvent(it.id, it.collect.not())))
                }
            }
        }

        binding.apply {
            with(pageLayout) {
                setRetryClickListener {
                    viewModel.uiAction.send(SquareUiAction.InitPageData)
                }
            }

            with(refreshLayout) {
                setOnRefreshListener {
                    viewModel.uiAction.send(SquareUiAction.RequestArticleData(true))
                }

                setOnLoadMoreListener {
                    viewModel.uiAction.send(SquareUiAction.RequestArticleData(false))
                }
            }

            with(rvArticleList) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                // 移除动画,优化黑夜模式切换时，界面恢复导致的列表动画闪烁
                itemAnimator = null
                adapter = articleAdapter
            }
        }

        NetworkMonitor.observer(viewLifecycleOwner.lifecycle) { isConnected, _ ->
            if (binding.pageLayout.getViewStatus() == MultiStatusLayout.STATE_NO_NETWORK && isConnected) {
                viewModel.uiAction.send(SquareUiAction.InitPageData)
            }
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)

        viewModel.registerCommonEvent(this)

        observerState(viewModel.uiStateFlow.map { it.pageStatus }) {
            binding.pageLayout.showViewByStatus(it.status)
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
                    }
                }
            }
        }
    }

    override fun onFirstVisible() {
        viewModel.uiAction.send(SquareUiAction.InitPageData)
    }
}