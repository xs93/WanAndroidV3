package com.github.xs93.wanandroid.common.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.xs93.core.base.ui.viewbinding.BaseVbDialogFragment
import com.github.xs93.wanandroid.common.databinding.CommonLoadingDialogBinding

/**
 *
 * 加载弹出框
 *
 * @author xushuai
 * @date   2022/9/8-10:02
 * @email  466911254@qq.com
 */
class LoadingDialog : BaseVbDialogFragment<CommonLoadingDialogBinding>() {

    companion object {
        fun newInstance(): LoadingDialog {
            val args = Bundle()
            val fragment = LoadingDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): CommonLoadingDialogBinding {
        return CommonLoadingDialogBinding.inflate(inflater, container, false)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        dialog?.apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
    }
}