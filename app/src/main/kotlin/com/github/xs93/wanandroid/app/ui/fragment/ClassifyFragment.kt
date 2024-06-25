package com.github.xs93.wanandroid.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.FragmentClassifyBinding

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/22 14:24
 * @email 466911254@qq.com
 */
class ClassifyFragment :
    BaseViewBindingFragment<FragmentClassifyBinding>(R.layout.fragment_classify, FragmentClassifyBinding::bind) {

    companion object {
        fun newInstance(): ClassifyFragment {
            val args = Bundle()
            val fragment = ClassifyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}