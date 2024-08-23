package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentNavigatorChildTutorialBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 15:20
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class TutorialFragment : BaseViewBindingFragment<FragmentNavigatorChildTutorialBinding>(
    R.layout.fragment_navigator_child_tutorial,
    FragmentNavigatorChildTutorialBinding::bind
) {

    companion object {
        fun newInstance(): TutorialFragment {
            val args = Bundle()
            val fragment = TutorialFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}