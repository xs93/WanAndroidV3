package com.github.xs93.framework.base.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.xs93.utils.AppInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 *
 * 基础ViewModel对象
 *
 * @author xushuai
 * @date   2022/5/5-21:21
 * @email  466911254@qq.com
 */
abstract class BaseViewModel<UiAction : IUiAction, UiState : IUIState, UiEvent : IUiEvent> :
    ViewModel() {

    private val _commonEventFlow: Channel<CommonUiEvent> = Channel(Channel.UNLIMITED)
    val commonEventFlow = _commonEventFlow.receiveAsFlow()

    private val _uiActionFlow: Channel<UiAction> = Channel(Channel.UNLIMITED)
    private val uiActionFlow = _uiActionFlow.receiveAsFlow()

    //state 保存UI状态
    private val _uiStateFlow: MutableStateFlow<UiState> by lazy { MutableStateFlow(initUiState()) }
    val uiStateFlow: StateFlow<UiState> by lazy { _uiStateFlow }

    //一次性消费事件，如toast，显示关闭弹窗等消息
    private val _uiEventFlow: Channel<UiEvent> = Channel(Channel.UNLIMITED)
    val uiEventFlow = _uiEventFlow.receiveAsFlow()

    protected abstract fun initUiState(): UiState

    protected abstract fun handleAction(action: UiAction)

    fun sendUiIntent(intent: UiAction) {
        viewModelScope.launch {
            _uiActionFlow.send(intent)
        }
    }

    protected fun setUiState(copy: UiState.() -> UiState) {
        _uiStateFlow.update {
            copy(_uiStateFlow.value)
        }
    }

    protected suspend fun sendUiEvent(uiEvent: UiEvent) {
        _uiEventFlow.send(uiEvent)
    }

    protected fun getString(@StringRes resId: Int, vararg any: Any?): String {
        return AppInject.getApp().getString(resId, any)
    }

    protected fun showToast(charSequence: CharSequence) {
        viewModelScope.launch {
            _commonEventFlow.send(CommonUiEvent.ShowToast(charSequence))
        }
    }

    protected fun showToast(@StringRes resId: Int, vararg any: Any?) {
        viewModelScope.launch {
            _commonEventFlow.send(
                CommonUiEvent.ShowToast(
                    com.github.xs93.utils.AppInject.getApp().getString(resId, any)
                )
            )
        }
    }

    protected fun showLoadingDialog() {
        viewModelScope.launch {
            _commonEventFlow.send(CommonUiEvent.ShowLoadingDialog)
        }
    }

    protected fun hideLoadingDialog() {
        viewModelScope.launch {
            _commonEventFlow.send(CommonUiEvent.HideLoadingDialog)
        }
    }

    init {
        viewModelScope.launch {
            uiActionFlow.collect {
                handleAction(it)
            }
        }
    }
}
