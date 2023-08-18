package com.github.xs93.wanandroid.app.ui.home.child.square

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.SquareFragmentBinding

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 14:30
 * @email 466911254@qq.com
 */
class SquareFragment : BaseViewBindingFragment<SquareFragmentBinding>(
    R.layout.square_fragment,
    SquareFragmentBinding::bind
) {

    companion object {
        fun newInstance(): SquareFragment {
            val args = Bundle()
            val fragment = SquareFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}