package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentMineBinding

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:25
 * @email 466911254@qq.com
 */
class MineFragment : BaseViewBindingFragment<FragmentMineBinding>(R.layout.fragment_mine, FragmentMineBinding::bind) {

    companion object {
        fun newInstance(): MineFragment {
            val args = Bundle()
            val fragment = MineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}