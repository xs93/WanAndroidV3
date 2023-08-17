package com.github.xs93.framework.base.ui.databinding

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.github.xs93.framework.base.ui.function.BaseFunctionActivity

/**
 * 自动实现ViewBinding功能的Activity基础类,VB也可以使用DataBinding
 *
 *
 * @author xushuai
 * @date   2022/3/26-16:47
 * @email  466911254@qq.com
 */
abstract class BaseDataBindingActivity<VB : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    BaseFunctionActivity() {

    protected lateinit var binding: VB

    override fun getContentLayoutId(): Int {
        return 0
    }

    override fun getContentView(): View? {
        return null
    }

    override fun customSetContentView() {
        super.customSetContentView()
        binding = DataBindingUtil.setContentView(this, layoutId)
    }
}