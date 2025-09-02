package com.github.xs93.framework.base.ui.viewbinding

import android.view.LayoutInflater
import android.view.View
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

    protected lateinit var viewBinding: VB

    final override val contentLayoutId: Int = 0

    final override fun getContentView(): View? {
        viewBinding = inflate(layoutInflater)
        return viewBinding.root
    }
}