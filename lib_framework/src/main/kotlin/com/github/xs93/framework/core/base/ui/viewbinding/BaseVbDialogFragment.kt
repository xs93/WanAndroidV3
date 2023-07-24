package com.github.xs93.framework.core.base.ui.viewbinding

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.github.xs93.framework.core.base.ui.function.BaseFunctionDialogFragment

/**
 *
 * 可以使用ViewBinding和dataBinding的DialogFragment
 *
 * @author xushuai
 * @date   2022/5/15-15:38
 * @email  466911254@qq.com
 */
abstract class BaseVbDialogFragment<VB : ViewDataBinding>(@LayoutRes val layoutId: Int) : BaseFunctionDialogFragment() {

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