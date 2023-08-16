package com.github.xs93.framework.base.ui.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.github.xs93.framework.base.ui.function.BaseFunctionFragment

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

    protected lateinit var binding: VB

    override fun getContentLayoutId(): Int {
        return layoutId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getContentLayoutId(), container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}