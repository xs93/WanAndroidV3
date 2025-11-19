package com.github.xs93.demo

import android.os.Bundle
import com.github.xs93.demo.databinding.ActivityCornerLayoutTestBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.utils.ktx.dp

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/6/16 15:41
 * @description
 *
 */
class CornerConstraintLayoutTestActivity : BaseVBActivity<ActivityCornerLayoutTestBinding>(
    ActivityCornerLayoutTestBinding::inflate
) {
    override fun initView(savedInstanceState: Bundle?) {
        vBinding.cclLayout.setRadius(10.dp(), 20.dp(), 30.dp(), 40.dp())
    }
}