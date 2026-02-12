package com.github.xs93.wan.home.fragment

import android.os.Bundle
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import com.github.xs93.framework.adapter.SimpleViewPagerAdapter
import com.github.xs93.framework.base.ui.viewbinding.BaseVBFragment
import com.github.xs93.framework.ktx.setTouchSlopMultiple
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.ktx.string
import com.github.xs93.utils.ktx.viewLifecycle
import com.github.xs93.wan.common.viewmodel.MainAction
import com.github.xs93.wan.common.viewmodel.MainViewModel
import com.github.xs93.wan.data.entity.HomeTabBean
import com.github.xs93.wan.home.R
import com.github.xs93.wan.home.databinding.HomeFragmentHomeBinding
import com.github.xs93.wan.router.PageRouterPath
import com.google.android.material.tabs.TabLayoutMediator
import com.therouter.router.Route

/**
 * 首页 fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 13:47
 * @email 466911254@qq.com
 */
@Route(path = PageRouterPath.FRAGMENT_HOME)
class HomeFragment : BaseVBFragment<HomeFragmentHomeBinding>(HomeFragmentHomeBinding::inflate) {

    private lateinit var childFragmentAdapter: SimpleViewPagerAdapter
    private val homeTabs = generateHomeTabs()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun initView(view: View, savedInstanceState: Bundle?) {

        childFragmentAdapter = SimpleViewPagerAdapter(childFragmentManager, viewLifecycle).apply {
            homeTabs.forEach {
                when (it.titleResId) {
                    R.string.home_tab_explore -> add { ExploreFragment.newInstance() }
                    R.string.home_tab_square -> add { SquareFragment.newInstance() }
                    R.string.home_tab_answer -> add { AnswerFragment.newInstance() }
                }
            }
        }

        vBinding.apply {
            with(vpContent) {
                offscreenPageLimit = homeTabs.size
                adapter = childFragmentAdapter
                setTouchSlopMultiple(2.5f)
            }

            TabLayoutMediator(tabLayout, vpContent) { tab, position ->
                tab.text = string(homeTabs[position].titleResId)
            }.attach()

            with(ivOpenDrawer) {
                setSingleClickListener {
                    mainViewModel.mainActions.send(MainAction.OpenDrawerAction)
                }
            }
        }
    }


    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        vBinding.clToolbar.updatePadding(top = insets.top)
    }


    private fun generateHomeTabs() = listOf(
        HomeTabBean(R.string.home_tab_explore),
        HomeTabBean(R.string.home_tab_square),
        HomeTabBean(R.string.home_tab_answer)
    )
}