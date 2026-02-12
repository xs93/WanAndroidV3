package com.github.xs93.demo

import android.os.Bundle
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import com.github.xs93.demo.databinding.ActivityMainBinding
import com.github.xs93.ui.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.ktx.startActivitySafe

class MainActivity : BaseVBActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {
    override fun initView(savedInstanceState: Bundle?) {
        AppInject.init(this.application)
        vBinding.btnSoftInput.setSingleClickListener {
            startActivitySafe<SoftKeyboardTestActivity>()
        }

        vBinding.btnInterval.setSingleClickListener {
            startActivitySafe<IntervalTestActivity>()
        }
        vBinding.btnCamera.setSingleClickListener {
            startActivitySafe<CameraTestActivity>()
        }
        vBinding.btnBorderDrawable.setSingleClickListener {
            startActivitySafe<BorderDrawableActivity>()
        }
        vBinding.btnStore.setSingleClickListener {
            startActivitySafe<StoreTestActivity>()
        }
        vBinding.btnProgress.setSingleClickListener {
            startActivitySafe<ProgressViewTestActivity>()
        }
        vBinding.btnCornerLayout.setSingleClickListener {
            startActivitySafe<CornerConstraintLayoutTestActivity>()
        }
        vBinding.btnCrash.setSingleClickListener {
            throw RuntimeException("测试崩溃")
        }
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        vBinding.root.updatePadding(top = insets.top, bottom = insets.bottom)
    }
}