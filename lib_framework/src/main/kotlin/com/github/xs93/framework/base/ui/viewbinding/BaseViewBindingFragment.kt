package com.github.xs93.framework.base.ui.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
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
abstract class BaseViewBindingFragment<VB : ViewBinding>(
    @param:LayoutRes val layoutId: Int,
    val bind: (View) -> VB
) : BaseFunctionFragment() {

    protected lateinit var binding: VB

    override fun getContentLayoutId(): Int {
        return layoutId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)?.also {
            binding = bind(it)
        }
        return view
    }


}