package com.github.xs93.demo

import android.os.Bundle
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import com.github.xs93.demo.databinding.ActivityMainBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
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
        binding.btnProgress.setSingleClickListener {
            startActivitySafe<ProgressViewTestActivity>()
        }
        binding.btnCornerLayout.setSingleClickListener {
            startActivitySafe<CornerConstraintLayoutTestActivity>()
        }
        binding.btnCrash.setSingleClickListener {
            throw RuntimeException("测试崩溃")
        }
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        binding.root.updatePadding(top = insets.top, bottom = insets.bottom)
    }
}