package com.github.xs93.wanandroid.app.ui.classify

import android.os.Bundle
import android.view.View
import com.github.xs93.framework.core.base.ui.viewbinding.BaseVbFragment
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
class ClassifyFragment : BaseVbFragment<FragmentClassifyBinding>(R.layout.fragment_classify) {

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