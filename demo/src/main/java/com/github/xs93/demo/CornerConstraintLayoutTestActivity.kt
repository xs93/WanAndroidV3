package com.github.xs93.demo

import android.os.Bundle
import com.github.xs93.demo.databinding.ActivityCornerLayoutTestBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.utils.ktx.dp

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/6/16 15:41
 * @description
 *
 */
class CornerConstraintLayoutTestActivity :
    BaseViewBindingActivity<ActivityCornerLayoutTestBinding>(
        R.layout.activity_corner_layout_test,
        ActivityCornerLayoutTestBinding::bind
    ) {
    override fun initView(savedInstanceState: Bundle?) {
        binding.cclLayout.setRadius(10.dp(), 20.dp(), 30.dp(), 40.dp())
    }
}