package com.github.xs93.demo

import android.os.Bundle
import android.util.Log
import com.github.xs93.demo.databinding.ActivityProgressBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseVBActivity

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/5/19 15:41
 * @description
 *
 */
class ProgressViewTestActivity : BaseVBActivity<ActivityProgressBinding>(
    ActivityProgressBinding::inflate
) {

    override fun initView(savedInstanceState: Bundle?) {
        Log.d("aaaaa", "initView: ")
    }
}