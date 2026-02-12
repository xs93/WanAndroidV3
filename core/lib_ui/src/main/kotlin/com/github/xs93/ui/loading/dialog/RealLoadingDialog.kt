package com.github.xs93.ui.loading.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.github.xs93.ui.base.ui.viewbinding.BaseVBDialogFragment
import com.github.xs93.ui.databinding.BaseLayoutLoadingBinding

/**
 * 加载弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/7/26 17:27
 * @email 466911254@qq.com
 */
class RealLoadingDialog :
    BaseVBDialogFragment<BaseLayoutLoadingBinding>(BaseLayoutLoadingBinding::inflate) {

    companion object {
        fun newInstance(): RealLoadingDialog {
            val args = Bundle()
            val fragment = RealLoadingDialog()
            fragment.arguments = args
            return fragment
        }
    }


    @SuppressLint("DiscouragedApi")
    override fun initView(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
    }
}