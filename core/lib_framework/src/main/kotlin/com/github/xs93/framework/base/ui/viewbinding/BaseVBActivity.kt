package com.github.xs93.framework.base.ui.viewbinding

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.github.xs93.framework.base.ui.function.BaseFunctionActivity

/**
 * 使用ViewBinding 的 Activity
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 15:26
 * @email 466911254@qq.com
 */
abstract class BaseVBActivity<VB : ViewBinding>(private val inflate: (inflater: LayoutInflater) -> VB) :
    BaseFunctionActivity() {

    protected lateinit var vBinding: VB

    final override val contentLayoutId: Int = 0

    override fun customSetContentView() {
        vBinding = inflate(layoutInflater)
        setContentView(vBinding.root)
    }

    fun withVBinding(block: VB.() -> Unit) {
        block.invoke(vBinding)
    }
}