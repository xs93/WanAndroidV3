package com.github.xs93.demo.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePaddingRelative
import com.github.xs93.demo.R
import com.github.xs93.demo.databinding.DialogSoftKeyboardTestBinding
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingDialogFragment

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/1/27 9:27
 * @email 466911254@qq.com
 */
class SoftKeyboardTestDialog : BaseViewBindingDialogFragment<DialogSoftKeyboardTestBinding>(
    R.layout.dialog_soft_keyboard_test,
    DialogSoftKeyboardTestBinding::bind
) {

    companion object {
        fun newInstance(): SoftKeyboardTestDialog {
            val args = Bundle()
            val fragment = SoftKeyboardTestDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        val gravity = dialog?.window?.attributes?.gravity ?: 0
        if (isImmersive() && (isFullScreen() || (gravity and Gravity.BOTTOM) == Gravity.BOTTOM)) {
            binding.layoutBottom.updatePaddingRelative(bottom = insets.bottom)
        }
    }

    override fun onSoftKeyboardHeightChanged(imeVisible: Boolean, height: Int) {
        super.onSoftKeyboardHeightChanged(imeVisible, height)
        binding.spaceKeyboard.updateLayoutParams {
            this.height = height
        }
    }

    override fun isImmersive() = true
    override fun isFullScreen() = true
    override fun isBottomDialog() = false

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}