package com.github.xs93.framework.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * MVI State Event消息封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/8 9:18
 * @email 466911254@qq.com
 */


interface IMVIEventContainer<EVENT : IUiEvent> {

    /**
     * Ui 状态流
     */
    val uiEventFlow: Flow<EVENT>
}

interface MutableMVIEventContainer<EVENT : IUiEvent> : IMVIEventContainer<EVENT> {
    fun sendEvent(event: EVENT)
}

internal class RealMVIEventContainer<EVENT : IUiEvent>(private val scope: CoroutineScope) :
    MutableMVIEventContainer<EVENT> {
    private val _uiEventFlow: Channel<EVENT> = Channel(Channel.UNLIMITED)
    override val uiEventFlow: Flow<EVENT> = _uiEventFlow.receiveAsFlow()
    override fun sendEvent(event: EVENT) {
        scope.launch {
            _uiEventFlow.send(event)
        }
    }
}

class MVIEventContainerLazy<EVENT : IUiEvent>(scope: CoroutineScope) :
    Lazy<MutableMVIEventContainer<EVENT>> {

    private var cached: MutableMVIEventContainer<EVENT>? = null

    override val value: MutableMVIEventContainer<EVENT> =
        cached ?: RealMVIEventContainer<EVENT>(scope).also { cached = it }

    override fun isInitialized(): Boolean {
        return cached != null
    }
}

fun <EVENT : IUiEvent> ViewModel.mviEvents(): Lazy<MutableMVIEventContainer<EVENT>> {
    return MVIEventContainerLazy(viewModelScope)
}

