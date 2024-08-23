package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import com.github.xs93.framework.adapter.SimpleViewPagerAdapter
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.framework.ktx.setTouchSlopMultiple
import com.github.xs93.framework.ui.ContentPadding
import com.github.xs93.utils.ktx.string
import com.github.xs93.utils.ktx.viewLifecycle
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentNavigatorBinding
import com.github.xs93.wanandroid.common.model.SimpleTabBean
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * 导航Fragment
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:24
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class NavigatorFragment : BaseViewBindingFragment<FragmentNavigatorBinding>(
    R.layout.fragment_navigator,
    FragmentNavigatorBinding::bind
) {
    companion object {
        fun newInstance(): NavigatorFragment {
            val args = Bundle()
            val fragment = NavigatorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val tabs = generateTabs()
    private lateinit var childFragmentAdapter: SimpleViewPagerAdapter

    override fun initView(view: View, savedInstanceState: Bundle?) {
        childFragmentAdapter = SimpleViewPagerAdapter(childFragmentManager, viewLifecycle).apply {
            tabs.forEach {
                when (it.titleResId) {
                    R.string.navigator_tab_navigator -> add { NavigatorChildFragment.newInstance() }
                    R.string.navigator_tab_series -> add { SeriesFragment.newInstance() }
                    R.string.navigator_tab_tutorial -> add { TutorialFragment.newInstance() }
                }
            }
        }

        binding.apply {
            with(vpContent) {
                offscreenPageLimit = tabs.size - 1
                adapter = childFragmentAdapter
                setTouchSlopMultiple(2.5f)
            }

            TabLayoutMediator(tabLayout, vpContent) { tab, position ->
                tabs[position].titleResId?.let {
                    tab.text = string(it)
                }
            }.attach()
        }
    }

    override fun onSystemBarInsetsChanged(contentPadding: ContentPadding) {
        super.onSystemBarInsetsChanged(contentPadding)
        binding.tabLayout.updatePadding(top = contentPadding.top)
    }

    private fun generateTabs() = listOf(
        SimpleTabBean(R.string.navigator_tab_navigator),
        SimpleTabBean(R.string.navigator_tab_series),
        SimpleTabBean(R.string.navigator_tab_tutorial)
    )
}