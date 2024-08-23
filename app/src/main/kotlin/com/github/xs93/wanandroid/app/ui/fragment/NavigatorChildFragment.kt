package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentNavigatorChildNavigatorBinding
import com.github.xs93.wanandroid.app.ui.viewmodel.NavigatorChildUiAction
import com.github.xs93.wanandroid.app.ui.viewmodel.NavigatorChildViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun onFirstVisible() {
        viewModel.uiAction.send(NavigatorChildUiAction.InitPageData)
    }
}