package com.github.xs93.wanandroid.app.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.view.updateLayoutParams
import com.github.xs93.framework.base.ui.interfaces.ISoftKeyboardListener
import com.github.xs93.framework.base.ui.interfaces.SoftKeyboardInsetsCallback
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingDialogFragment
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.DialogSoftKeyboardTestBinding

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

    private var softKeyboardInsetsCallback: SoftKeyboardInsetsCallback? = null

    override fun getCustomStyle(): Int {
        return com.github.xs93.framework.R.style.BaseDialogTheme_FullScreen_Immersive
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val layoutParams = it.attributes
            layoutParams.width = -1
            layoutParams.height = -1
            it.attributes = layoutParams
            it.setGravity(Gravity.BOTTOM)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        softKeyboardInsetsCallback = SoftKeyboardInsetsCallback(
            listener = object : ISoftKeyboardListener {
                override fun onSoftKeyboardChanged(show: Boolean, height: Int) {
                    binding.spaceKeyboard.updateLayoutParams {
                        this.height = height
                    }
                    Log.d(
                        "SoftKeyboardInsetsCallback-dialog",
                        "onSoftKeyboardChanged: $show,$height"
                    )
                }
            }
        )
        softKeyboardInsetsCallback?.attachToView(binding.editText)
    }
}