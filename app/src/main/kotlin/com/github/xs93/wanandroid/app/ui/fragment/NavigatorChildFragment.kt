package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.framework.ktx.observerState
import com.github.xs93.framework.ui.layoutManager.CenterLinearLayoutManager
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentNavigatorChildNavigatorBinding
import com.github.xs93.wanandroid.app.ui.adapter.NavigatorChipAdapter
import com.github.xs93.wanandroid.app.ui.adapter.NavigatorChipChildrenAdapter
import com.github.xs93.wanandroid.app.ui.viewmodel.NavigatorChildUiAction
import com.github.xs93.wanandroid.app.ui.viewmodel.NavigatorChildViewModel
import com.github.xs93.wanandroid.common.entity.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

/**
 *  导航内容Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 15:00
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class NavigatorChildFragment : BaseViewBindingFragment<FragmentNavigatorChildNavigatorBinding>(
    R.layout.fragment_navigator_child_navigator,
    FragmentNavigatorChildNavigatorBinding::bind
) {

    companion object {
        fun newInstance(): NavigatorChildFragment {
            val args = Bundle()
            val fragment = NavigatorChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: NavigatorChildViewModel by viewModels()

    private lateinit var chipAdapter: NavigatorChipAdapter
    private lateinit var chipLayoutManager: CenterLinearLayoutManager

    private lateinit var chipChildrenAdapter: NavigatorChipChildrenAdapter

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            with(rvChipList) {
                adapter = NavigatorChipAdapter()
                    .apply {
                        setOnItemClickListener(object :
                            BaseQuickAdapter.OnItemClickListener<Navigation> {
                            override fun onClick(
                                adapter: BaseQuickAdapter<Navigation, *>,
                                view: View,
                                position: Int,
                            ) {
                                val item = getItem(position)
                                item?.let { setSelectedNavigation(it) }
                            }
                        })
                    }
                    .also { chipAdapter = it }
                layoutManager = CenterLinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                ).also { chipLayoutManager = it }
            }

            with(rvChipChildrenList) {
                adapter = NavigatorChipChildrenAdapter()
                    .also { chipChildrenAdapter = it }
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        observerState(viewModel.uiStateFlow.map { it.pageStatus }) {
            binding.pageLayout.showViewByStatus(it.status)
        }

        observerState(viewModel.uiStateFlow.map { it.navigationList }) {
            chipAdapter.submitList(it)
            chipChildrenAdapter.submitList(it)
        }
    }

    override fun onFirstVisible() {
        viewModel.uiAction.send(NavigatorChildUiAction.InitPageData)
    }
}