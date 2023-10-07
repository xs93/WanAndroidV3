package com.github.xs93.wanandroid.app.ui.home.child.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.QuickAdapterHelper
import com.github.xs93.framework.base.ui.databinding.BaseDataBindingFragment
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.observer
import com.github.xs93.framework.ktx.observerEvent
import com.github.xs93.statuslayout.MultiStatusLayout
import com.github.xs93.utils.ktx.viewLifecycle
import com.github.xs93.utils.net.NetworkMonitor
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ExploreFragmentBinding
import com.github.xs93.wanandroid.app.ui.home.child.HomeArticleAdapter
import com.github.xs93.wanandroid.web.WebActivity
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
class ExploreFragment : BaseDataBindingFragment<ExploreFragmentBinding>(R.layout.explore_fragment) {
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
            setOnItemClickListener { _, _, position ->
                val article = items[position]
                WebActivity.start(requireContext(), article.link, article.title)
            }
        }

        adapterHelper = QuickAdapterHelper.Builder(articleAdapter)
            .build()
            .addBeforeAdapter(bannerHeaderAdapter)

        binding.apply {
            with(pageLayout) {
                setRetryClickListener {
                    viewModel.exploreActions.sendAction(ExploreUiAction.InitPageData)
                }
            }

            with(refreshLayout) {
                setOnRefreshListener {
                    viewModel.exploreActions.sendAction(ExploreUiAction.RequestArticleData(true))
                }

                setOnLoadMoreListener {
                    viewModel.exploreActions.sendAction(ExploreUiAction.RequestArticleData(false))
                }
            }

            with(rvArticleList) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = adapterHelper.adapter
            }
        }

        NetworkMonitor.observer(viewLifecycleOwner.lifecycle) { isConnected, _ ->
            if (binding.pageLayout.getViewStatus() == MultiStatusLayout.STATE_NO_NETWORK && isConnected) {
                viewModel.exploreActions.sendAction(ExploreUiAction.InitPageData)
            }
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)

        viewModel.registerCommonEvent(this)

        observer(viewModel.exploreStateFlow.map { it.banners }) {
            bannerHeaderAdapter.item = it
        }

        observer(viewModel.exploreStateFlow.map { it.articles }) {
            articleAdapter.submitList(it)
        }

        observer(viewModel.exploreStateFlow.map { it.pageLoadStatus }) {
            binding.pageLayout.showViewByStatus(it.status)
        }

        observerEvent(viewModel.exploreEventFlow) {
            when (it) {
                is ExploreUiEvent.RequestArticleDataComplete -> {
                    with(binding.refreshLayout) {
                        if (it.finishRefresh) {
                            if (it.noMoreData) {
                                finishRefreshWithNoMoreData()
                            } else {
                                finishRefresh(it.requestSuccess)
                            }
                        }
                        if (it.finishLoadMore) {
                            if (it.noMoreData) {
                                finishLoadMoreWithNoMoreData()
                            } else {
                                finishLoadMore(it.requestSuccess)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onFirstVisible() {
        viewModel.exploreActions.sendAction(ExploreUiAction.InitPageData)
    }
}