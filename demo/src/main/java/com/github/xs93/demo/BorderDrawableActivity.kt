package com.github.xs93.demo

import android.os.Bundle
import androidx.core.view.updatePadding
import com.github.xs93.demo.databinding.ActivityBorderDrawableBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ui.ContentPadding

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/4/17 16:39
 * @description BorderDrawable边框
 *
 */
class BorderDrawableActivity : BaseViewBindingActivity<ActivityBorderDrawableBinding>(
    R.layout.activity_border_drawable,
    ActivityBorderDrawableBinding::bind
) {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onSystemBarInsetsChanged(contentPadding: ContentPadding) {
        super.onSystemBarInsetsChanged(contentPadding)
        binding.root.updatePadding(top = contentPadding.top, bottom = contentPadding.bottom)
    }
}