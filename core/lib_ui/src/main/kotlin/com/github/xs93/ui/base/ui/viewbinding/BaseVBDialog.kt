package com.github.xs93.ui.base.ui.viewbinding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.github.xs93.ui.R
import com.github.xs93.ui.base.ui.base.BaseDialog

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/11/19 17:03
 * @description
 *
 */
abstract class BaseVBDialog<VB : ViewBinding>(
    context: Context,
    themeResId: Int = R.style.BaseDialogTheme,
    private val inflate: (inflater: LayoutInflater) -> VB
) : BaseDialog(context, themeResId) {

    private var _vBinding: VB? = null
    protected val vBinding: VB get() = _vBinding!!

    override fun onCreateView(layoutInflater: LayoutInflater, savedInstanceState: Bundle?): View {
        _vBinding = inflate.invoke(layoutInflater)
        return _vBinding!!.root
    }

    fun withVBinding(block: VB.() -> Unit) {
        _vBinding?.block()
    }
}