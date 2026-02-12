package com.github.xs93.ui.base.viewmodel

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
interface IMVIStateContainer<STATE : IUiState> {

    /**
     * Ui 状态流
     */
    val flow: StateFlow<STATE>

    /**
     * 当前UIState值
     */
    val value: STATE get() = flow.value
}

interface MutableMVIStateContainer<STATE : IUiState> : IMVIStateContainer<STATE> {
    fun update(action: STATE.() -> STATE)
}

internal class RealMVIStateContainer<STATE : IUiState>(initialState: STATE) :
    MutableMVIStateContainer<STATE> {

    private val _uiStateFlow: MutableStateFlow<STATE> by lazy { MutableStateFlow(initialState) }
    override val flow: StateFlow<STATE> = _uiStateFlow.asStateFlow()

    override fun update(action: STATE.() -> STATE) {
        _uiStateFlow.update { action(_uiStateFlow.value) }
    }
}

class MVIStateContainerLazy<STATE : IUiState>(initialState: STATE) :
    Lazy<MutableMVIStateContainer<STATE>> {

    private var cached: MutableMVIStateContainer<STATE>? = null

    override val value: MutableMVIStateContainer<STATE> =
        cached ?: RealMVIStateContainer(initialState).also { cached = it }

    override fun isInitialized(): Boolean {
        return cached != null
    }
}

fun <STATE : IUiState> ViewModel.mviStates(initialState: STATE): Lazy<MutableMVIStateContainer<STATE>> {
    return MVIStateContainerLazy(initialState)
}