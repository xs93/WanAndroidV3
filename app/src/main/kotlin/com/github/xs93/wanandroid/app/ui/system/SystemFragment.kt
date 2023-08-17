package com.github.xs93.wanandroid.app.ui.system

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.base.ui.databinding.BaseDataBindingFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentSystemBinding

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:24
 * @email 466911254@qq.com
 */
class SystemFragment : BaseDataBindingFragment<FragmentSystemBinding>(R.layout.fragment_system) {
    companion object {
        fun newInstance(): SystemFragment {
            val args = Bundle()
            val fragment = SystemFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}