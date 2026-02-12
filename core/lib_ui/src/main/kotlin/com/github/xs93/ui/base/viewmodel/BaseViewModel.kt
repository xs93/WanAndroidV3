package com.github.xs93.ui.base.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.github.xs93.core.AppInject
import com.github.xs93.core.toast.IToast
import com.github.xs93.core.toast.UiToastProxy

/**
 *
 * 基础ViewModel对象
 *
 * @author xushuai
 * @date   2022/5/5-21:21
 * @email  466911254@qq.com
 */
abstract class BaseViewModel : ViewModel(), IToast by UiToastProxy() {

    private val _commonEventFlow by mviEvents<CommonUiEvent>()
    val commonEventFlow by lazy { _commonEventFlow.flow }

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return AppInject.getApp().getString(resId, *formatArgs)
    }

    protected fun showLoadingDialog() {
        _commonEventFlow.send(CommonUiEvent.ShowLoadingDialog)
    }

    protected fun hideLoadingDialog() {
        _commonEventFlow.send(CommonUiEvent.HideLoadingDialog)
    }
}