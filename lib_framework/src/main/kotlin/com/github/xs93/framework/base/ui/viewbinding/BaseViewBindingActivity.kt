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
abstract class BaseViewBindingActivity<VB : ViewBinding> : BaseFunctionActivity() {

    protected lateinit var binding: VB

    override fun getContentLayoutId(): Int {
        return 0
    }

    override fun getContentView(): View? {
        binding = onCreateViewBinding(layoutInflater)
        return binding.root
    }

    abstract fun onCreateViewBinding(inflater: LayoutInflater): VB
}