package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentNavigatorChildSeriesBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 体系Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/8/7 15:06
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class SeriesFragment : BaseViewBindingFragment<FragmentNavigatorChildSeriesBinding>(
    R.layout.fragment_navigator_child_series,
    FragmentNavigatorChildSeriesBinding::bind
) {

    companion object {
        fun newInstance(): SeriesFragment {
            val args = Bundle()
            val fragment = SeriesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}