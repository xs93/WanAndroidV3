package com.github.xs93.framework.base.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * MVI State 状态封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/8 8:50
 * @email 466911254@qq.com
 */
interface IMVIStateContainer<STATE : IUIState> {

    /**
     * Ui 状态流
     */
    val uiStateFlow: StateFlow<STATE>
}

interface MutableMVIStateContainer<STATE : IUIState> : IMVIStateContainer<STATE> {
    fun updateState(action: STATE.() -> STATE)
}

internal class RealMVIStateContainer<STATE : IUIState>(initialState: STATE) :
    MutableMVIStateContainer<STATE> {

    private val _uiStateFlow: MutableStateFlow<STATE> by lazy { MutableStateFlow(initialState) }
    override val uiStateFlow: StateFlow<STATE> = _uiStateFlow.asStateFlow()

    override fun updateState(action: STATE.() -> STATE) {
        _uiStateFlow.update { action(_uiStateFlow.value) }
    }
}

class MVIStateContainerLazy<STATE : IUIState>(initialState: STATE) :
    Lazy<MutableMVIStateContainer<STATE>> {

    private var cached: MutableMVIStateContainer<STATE>? = null

    override val value: MutableMVIStateContainer<STATE> =
        cached ?: RealMVIStateContainer(initialState).also { cached = it }

    override fun isInitialized(): Boolean {
        return cached != null
    }
}

fun <STATE : IUIState> ViewModel.mviStates(initialState: STATE): Lazy<MutableMVIStateContainer<STATE>> {
    return MVIStateContainerLazy(initialState)
}