package com.github.xs93.wanandroid.app.ui.activity

import android.os.Bundle
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.github.xs93.framework.base.ui.interfaces.ISoftKeyboardListener
import com.github.xs93.framework.base.ui.interfaces.SoftKeyboardInsetsCallback
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ui.ContentPadding
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ActivitySoftKeyboardTestBinding
import com.github.xs93.wanandroid.app.ui.dialog.SoftKeyboardTestDialog

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/1/24 17:24
 * @email 466911254@qq.com
 */
class SoftKeyboardTestActivity : BaseViewBindingActivity<ActivitySoftKeyboardTestBinding>(
    R.layout.activity_soft_keyboard_test,
    ActivitySoftKeyboardTestBinding::bind
) {

    private var softKeyboardInsetsCallback: SoftKeyboardInsetsCallback? = null

    override fun initView(savedInstanceState: Bundle?) {

        softKeyboardInsetsCallback = SoftKeyboardInsetsCallback(
            tag = "activity",
            listener = object : ISoftKeyboardListener {
                override fun onSoftKeyboardChanged(show: Boolean, height: Int) {
                    binding.spaceKeyboard.updateLayoutParams {
                        this.height = height
                    }
                }
            }
        )
        softKeyboardInsetsCallback?.attachToView(binding.editText)

        binding.btnNormalDialog.setSingleClickListener {
            SoftKeyboardTestDialog.newInstance().showAllowingStateLoss(supportFragmentManager)
        }
    }

    override fun onSystemBarInsetsChanged(contentPadding: ContentPadding) {
        super.onSystemBarInsetsChanged(contentPadding)
        binding.root.updatePadding(bottom = contentPadding.bottom, top = contentPadding.top)
    }
}