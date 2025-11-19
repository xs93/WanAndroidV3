package com.github.xs93.framework.base.ui.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.github.xs93.framework.base.ui.function.BaseFunctionDialogFragment

/**
 *
 * 使用ViewBinding 的DialogFragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 15:31
 * @email 466911254@qq.com
 */
abstract class BaseVBDialogFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFunctionDialogFragment() {

    private var _vBinding: VB? = null
    protected val vBinding: VB get() = _vBinding!!

    final override val contentLayoutId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _vBinding = inflate.invoke(inflater, container, false)
        return _vBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vBinding = null
    }

    fun withVBinding(block: VB.() -> Unit) {
        _vBinding?.block()
    }
}