package com.github.xs93.demo

import android.os.Bundle
import androidx.core.view.updatePadding
import com.github.xs93.demo.databinding.ActivityMainBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ui.ContentPadding
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.ktx.startActivitySafe

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>(
    R.layout.activity_main,
    ActivityMainBinding::bind
) {
    override fun initView(savedInstanceState: Bundle?) {

        binding.btnSoftInput.setSingleClickListener {
            startActivitySafe<SoftKeyboardTestActivity>()
        }

        binding.btnInterval.setSingleClickListener {
            startActivitySafe<IntervalTestActivity>()
        }
    }

    override fun onSystemBarInsetsChanged(contentPadding: ContentPadding) {
        super.onSystemBarInsetsChanged(contentPadding)
        binding.root.updatePadding(top = contentPadding.top, bottom = contentPadding.bottom)
    }
}