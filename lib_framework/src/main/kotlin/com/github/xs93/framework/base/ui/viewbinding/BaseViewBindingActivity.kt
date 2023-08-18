package com.github.xs93.framework.base.ui.viewbinding

import android.view.View
import androidx.annotation.LayoutRes
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
abstract class BaseViewBindingActivity<VB : ViewBinding>(
    @LayoutRes val layoutId: Int,
    val bind: (View) -> VB
) : BaseFunctionActivity() {

    protected lateinit var binding: VB

    override fun getContentLayoutId(): Int {
        return 0
    }

    override fun getContentView(): View? {
        val view = layoutInflater.inflate(layoutId, null)
        binding = bind(view)
        return view
    }
}