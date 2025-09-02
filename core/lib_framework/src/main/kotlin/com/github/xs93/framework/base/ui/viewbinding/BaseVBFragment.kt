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
abstract class BaseVBFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFunctionFragment() {

    private var _viewBinding: VB? = null
    protected val viewBinding: VB get() = _viewBinding!!

    final override val contentLayoutId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = inflate.invoke(inflater, container, false)
        return _viewBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}