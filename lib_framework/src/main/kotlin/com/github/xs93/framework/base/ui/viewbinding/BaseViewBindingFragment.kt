package com.github.xs93.framework.base.ui.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.xs93.framework.base.ui.function.BaseFunctionFragment

/**
 * 使用ViewBinding 的Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 13:33
 * @email 466911254@qq.com
 */
abstract class BaseViewBindingFragment<VB : ViewBinding> : BaseFunctionFragment() {

    protected lateinit var binding: VB

    override fun getContentLayoutId(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = onCreateViewBinding(inflater, container)
        return binding.root
    }

    abstract fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}