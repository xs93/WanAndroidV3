package com.github.xs93.demo

import android.os.Bundle
import androidx.core.view.updatePadding
import com.github.xs93.demo.databinding.ActivityMainBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ui.ContentPadding
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.ktx.startActivitySafe

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>(
    R.layout.activity_main,
    ActivityMainBinding::bind
) {
    override fun initView(savedInstanceState: Bundle?) {
        AppInject.init(this.application)
        binding.btnSoftInput.setSingleClickListener {
            startActivitySafe<SoftKeyboardTestActivity>()
        }

        binding.btnInterval.setSingleClickListener {
            startActivitySafe<IntervalTestActivity>()
        }
        binding.btnCamera.setSingleClickListener {
            startActivitySafe<CameraTestActivity>()
        }
        binding.btnBorderDrawable.setSingleClickListener {
            startActivitySafe<BorderDrawableActivity>()
        }
        binding.btnStore.setSingleClickListener {
            startActivitySafe<StoreTestActivity>()
        }
    }

    override fun onSystemBarInsetsChanged(contentPadding: ContentPadding) {
        super.onSystemBarInsetsChanged(contentPadding)
        binding.root.updatePadding(top = contentPadding.top, bottom = contentPadding.bottom)
    }
}