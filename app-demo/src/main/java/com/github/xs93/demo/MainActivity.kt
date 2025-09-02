package com.github.xs93.demo

import android.os.Bundle
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import com.github.xs93.demo.databinding.ActivityMainBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.ktx.startActivitySafe

class MainActivity : BaseVBActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {
    override fun initView(savedInstanceState: Bundle?) {
        AppInject.init(this.application)
        viewBinding.btnSoftInput.setSingleClickListener {
            startActivitySafe<SoftKeyboardTestActivity>()
        }

        viewBinding.btnInterval.setSingleClickListener {
            startActivitySafe<IntervalTestActivity>()
        }
        viewBinding.btnCamera.setSingleClickListener {
            startActivitySafe<CameraTestActivity>()
        }
        viewBinding.btnBorderDrawable.setSingleClickListener {
            startActivitySafe<BorderDrawableActivity>()
        }
        viewBinding.btnStore.setSingleClickListener {
            startActivitySafe<StoreTestActivity>()
        }
        viewBinding.btnProgress.setSingleClickListener {
            startActivitySafe<ProgressViewTestActivity>()
        }
        viewBinding.btnCornerLayout.setSingleClickListener {
            startActivitySafe<CornerConstraintLayoutTestActivity>()
        }
        viewBinding.btnCrash.setSingleClickListener {
            throw RuntimeException("测试崩溃")
        }
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        viewBinding.root.updatePadding(top = insets.top, bottom = insets.bottom)
    }
}