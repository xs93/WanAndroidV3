package com.github.xs93.demo

import android.os.Bundle
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.github.xs93.core.ktx.showAllowingStateLoss
import com.github.xs93.demo.databinding.ActivitySoftKeyboardTestBinding
import com.github.xs93.demo.dialog.SoftKeyboardTestDialog
import com.github.xs93.ui.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.utils.ktx.setSingleClickListener

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/1/24 17:24
 * @email 466911254@qq.com
 */
class SoftKeyboardTestActivity : BaseVBActivity<ActivitySoftKeyboardTestBinding>(
    ActivitySoftKeyboardTestBinding::inflate
) {

    override fun initView(savedInstanceState: Bundle?) {
        vBinding.btnNormalDialog.setSingleClickListener {
            SoftKeyboardTestDialog.newInstance().showAllowingStateLoss(supportFragmentManager)
        }
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        vBinding.root.updatePadding(bottom = insets.bottom, top = insets.top)
    }

    override fun onSoftKeyboardHeightChanged(imeVisible: Boolean, height: Int) {
        super.onSoftKeyboardHeightChanged(imeVisible, height)
        vBinding.spaceKeyboard.updateLayoutParams {
            this.height = height
        }
    }
}