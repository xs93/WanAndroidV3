package com.github.xs93.framework.core.base.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.github.xs93.framework.core.utils.AppInject
import com.github.xs93.framework.network.exception.ExceptionHandler
import com.orhanobut.logger.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 *
 * 基础ViewModel对象
 *
 * @author xushuai
 * @date   2022/5/5-21:21
 * @email  466911254@qq.com
 */
abstract class BaseViewModel<UiIntent : IUiAction, UiState : IUIState, UiEvent : IUiEvent> : ViewModel() {

    companion object {
        @JvmStatic
        fun createViewModelFactory(viewModel: ViewModel): ViewModelFactory {
            return ViewModelFactory(viewModel)
        }
    }

    //state 保存UI状态
    private val _uiStateFlow: MutableStateFlow<UiState> by lazy {
        MutableStateFlow(initUiState())
    }
    val uiStateFlow: StateFlow<UiState> by lazy {
        _uiStateFlow
    }

    protected abstract fun initUiState(): UiState

    //一次性消费事件，如toast，显示关闭弹窗等消息
    private val _uiEventFlow: Channel<UiEvent> = Channel(Channel.UNLIMITED)
    val uiEventFlow = _uiEventFlow.receiveAsFlow()

    private val _commonEventFlow: Channel<CommonUiEvent> = Channel(Channel.UNLIMITED)
    val commonEventFlow = _commonEventFlow.receiveAsFlow()

    private val _uiIntentFlow: Channel<UiIntent> = Channel(Channel.UNLIMITED)
    private val uiIntentFlow = _uiIntentFlow.receiveAsFlow()

    protected abstract fun handleIntent(intent: UiIntent)

    fun sendUiIntent(intent: UiIntent) {
        viewModelScope.launch {
            _uiIntentFlow.send(intent)
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
            _commonEventFlow.send(CommonUiEvent.ShowToast(AppInject.getApp().getString(resId, any)))
        }
    }

    protected fun showLoadingDialog(message: CharSequence? = null) {
        viewModelScope.launch {
            _commonEventFlow.send(CommonUiEvent.ShowLoadingDialog(message))
        }
    }

    protected fun updateLoadingDialog(message: CharSequence) {
        viewModelScope.launch {
            _commonEventFlow.send(CommonUiEvent.ShowLoadingDialog(message))
        }
    }

    protected fun hideLoadingDialog() {
        viewModelScope.launch {
            _commonEventFlow.send(CommonUiEvent.HideLoadingDialog)
        }
    }

    protected suspend fun <T> safeRequestApi(
        errorBlock: ((Throwable) -> Unit)? = ExceptionHandler.safeRequestApiErrorHandler,
        block: suspend () -> T?
    ): T? {
        return try {
            block()
        } catch (e: Throwable) {
            val ex = ExceptionHandler.handleException(e)
            errorBlock?.invoke(ex)
            Logger.e(ex, "safeRequestApi")
            null
        }
    }


    init {
        viewModelScope.launch {
            uiIntentFlow.collect {
                handleIntent(it)
            }
        }
    }
}

/**
 * ViewModel 工厂，这样可以外部传递参数给ViewModel
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val viewModel: ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
