package com.github.xs93.ui.base.ui.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.github.xs93.ui.base.ui.base.BaseActivity

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/4/30
 * @description 使用ViewBinding 的BaseActivity
 *
 */
abstract class BaseVBActivity<VB : ViewBinding>(
    private val inflate: (inflater: LayoutInflater) -> VB
) : BaseActivity() {

    private var _vBinding: VB? = null
    protected val vBinding: VB get() = _vBinding!!

    final override val contentLayoutId: Int = 0

    override fun customSetContentView(savedInstanceState: Bundle?) {
        _vBinding = inflate(layoutInflater)
        setContentView(vBinding.root)
    }

    fun withVBinding(block: VB.() -> Unit) {
        _vBinding?.block()
    }
}