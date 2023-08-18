package com.github.xs93.wanandroid.app.ui.home.child.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.QuickAdapterHelper
import com.github.xs93.framework.base.ui.databinding.BaseDataBindingFragment
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.observer
import com.github.xs93.utils.ktx.viewLifecycle
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ExploreFragmentBinding
import com.github.xs93.wanandroid.app.ui.home.child.HomeArticleAdapter
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


    override fun initView(view: View, savedInstanceState: Bundle?) {
        bannerHeaderAdapter = ExploreBannerHeaderAdapter(viewLifecycle)
        articleAdapter = HomeArticleAdapter()

        val helper = QuickAdapterHelper.Builder(articleAdapter).build()
        helper.addBeforeAdapter(bannerHeaderAdapter)

        binding.apply {
            with(rvArticleList) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = helper.adapter
            }
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)

        viewModel.registerCommonEvent(this)

        observer(viewModel.uiStateFlow.map { it.banners }) {
            bannerHeaderAdapter.item = it
        }

        observer(viewModel.uiStateFlow.map { it.articles }) {
            articleAdapter.submitList(it)
        }
    }

    override fun onFirstVisible() {
        viewModel.sendUiIntent(ExploreUiAction.InitBannerData)
    }
}