package com.github.xs93.framework.base.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.github.xs93.utils.AppInject
import com.github.xs93.utils.ktx.string

/**
 *
 * 基础ViewModel对象
 *
 * @author xushuai
 * @date   2022/5/5-21:21
 * @email  466911254@qq.com
 */
abstract class BaseViewModel : ViewModel() {

    private val _commonEventFlow by mviEvents<CommonUiEvent>()
    val commonEventFlow by lazy { _commonEventFlow.uiEventFlow }

    protected fun getString(@StringRes resId: Int, vararg any: Any?): String {
        return AppInject.getApp().string(resId, any)
    }

    protected fun showToast(charSequence: CharSequence) {
        _commonEventFlow.sendEvent(CommonUiEvent.ShowToast(charSequence))
    }

    protected fun showToast(@StringRes resId: Int, vararg any: Any?) {
        showToast(getString(resId, any))
    }

    protected fun showLoadingDialog() {
        _commonEventFlow.sendEvent(CommonUiEvent.ShowLoadingDialog)
    }

    protected fun hideLoadingDialog() {
        _commonEventFlow.sendEvent(CommonUiEvent.HideLoadingDialog)
    }
}
