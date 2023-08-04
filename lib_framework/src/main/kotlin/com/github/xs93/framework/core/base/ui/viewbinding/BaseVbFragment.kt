package com.github.xs93.framework.core.base.ui.viewbinding

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.github.xs93.framework.core.base.ui.function.BaseFunctionFragment

/**
 *
 * 集成ViewBinding功能的Fragment基类,VB也可以使用DataBinding
 *
 * @author xushuai
 * @date   2022/3/26-17:04
 * @email  466911254@qq.com
 */
abstract class BaseVbFragment<VB : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    BaseFunctionFragment() {

    private var _mBinding: VB? = null
    protected val binding get() = _mBinding!!

    override fun getContentLayoutId(): Int {
        return layoutId
    }

    override fun beforeInitView(view: View, savedInstanceState: Bundle?) {
        super.beforeInitView(view, savedInstanceState)
        _mBinding = DataBindingUtil.bind(view)
        _mBinding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}