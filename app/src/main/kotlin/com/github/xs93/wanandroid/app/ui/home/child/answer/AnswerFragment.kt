package com.github.xs93.wanandroid.app.ui.home.child.answer

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.AnswerFragmentBinding

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/18 14:29
 * @email 466911254@qq.com
 */
class AnswerFragment : BaseViewBindingFragment<AnswerFragmentBinding>(
    R.layout.answer_fragment,
    AnswerFragmentBinding::bind
) {

    companion object {
        fun newInstance(): AnswerFragment {
            val args = Bundle()
            val fragment = AnswerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}