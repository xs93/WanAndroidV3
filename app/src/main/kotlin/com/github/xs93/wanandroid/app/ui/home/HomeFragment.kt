package com.github.xs93.wanandroid.app.ui.home

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.core.base.ui.viewbinding.BaseVbFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentHomeBinding

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:23
 * @email 466911254@qq.com
 */
class HomeFragment : BaseVbFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}