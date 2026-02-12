package com.github.xs93.demo.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePaddingRelative
import com.github.xs93.demo.databinding.DialogSoftKeyboardTestBinding
import com.github.xs93.ui.base.ui.viewbinding.BaseVBBottomSheetDialogFragment

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/1/27 9:27
 * @email 466911254@qq.com
 */
class SoftKeyboardTestDialog : BaseVBBottomSheetDialogFragment<DialogSoftKeyboardTestBinding>(
    DialogSoftKeyboardTestBinding::inflate
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
        Log.d("aaaaaa", "onSystemBarInsetsChanged: $insets")
        vBinding.root.updatePaddingRelative(bottom = insets.bottom)
    }

    override fun onSoftKeyboardHeightChanged(imeVisible: Boolean, height: Int) {
        super.onSoftKeyboardHeightChanged(imeVisible, height)
        Log.d("aaaaaa", "onSoftKeyboardHeightChanged: $imeVisible,$height")
        vBinding.spaceKeyboard.updateLayoutParams {
            this.height = height
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }
}