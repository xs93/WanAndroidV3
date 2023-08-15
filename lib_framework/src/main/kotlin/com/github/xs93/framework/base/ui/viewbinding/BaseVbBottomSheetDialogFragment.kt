package com.github.xs93.framework.base.ui.viewbinding

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.github.xs93.framework.base.ui.function.BaseFunctionBottomSheetDialogFragment

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/7/3 9:42
 * @email 466911254@qq.com
 */
abstract class BaseVbBottomSheetDialogFragment<VB : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    BaseFunctionBottomSheetDialogFragment() {

    private var _mBinding: VB? = null
    protected val binding get() = _mBinding!!

    override fun getContentLayoutId(): Int {
        return layoutId
    }

    override fun beforeInitView(view: View, savedInstanceState: Bundle?) {
        super.beforeInitView(view, savedInstanceState)
        _mBinding = DataBindingUtil.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}