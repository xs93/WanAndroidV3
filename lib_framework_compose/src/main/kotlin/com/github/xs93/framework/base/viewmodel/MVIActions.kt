package com.github.xs93.framework.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * MVIAction 处理
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/9/7 9:16
 * @email 466911254@qq.com
 */
interface IMVIActionContainer<ACTION : IUiAction> {

    val uiActionFlow: Flow<IUiAction>
}


interface MutableMVIActionContainer<ACTION : IUiAction> : IMVIActionContainer<ACTION> {

    fun sendAction(event: ACTION)
}


internal class RealMVIActionContainer<ACTION : IUiAction>(
    private val handler: (ACTION) -> Unit,
    private val scope: CoroutineScope
) : MutableMVIActionContainer<ACTION> {

    private val _uiActionFlow: Channel<ACTION> = Channel(Channel.UNLIMITED)
    override val uiActionFlow = _uiActionFlow.receiveAsFlow()
    override fun sendAction(event: ACTION) {
        scope.launch {
            _uiActionFlow.send(event)
        }
    }

    init {
        scope.launch {
            uiActionFlow.collect {
                handler(it)
            }
        }
    }
}

class MVIActionContainerLazy<ACTION : IUiAction>(handler: (ACTION) -> Unit, scope: CoroutineScope) :
    Lazy<MutableMVIActionContainer<ACTION>> {

    private var cached: MutableMVIActionContainer<ACTION>? = null

    override val value: MutableMVIActionContainer<ACTION> =
        cached ?: RealMVIActionContainer(handler, scope).also { cached = it }

    override fun isInitialized(): Boolean {
        return cached != null
    }
}

fun <ACTION : IUiAction> ViewModel.mviActions(handler: (ACTION) -> Unit): Lazy<MutableMVIActionContainer<ACTION>> {
    return MVIActionContainerLazy(handler, viewModelScope)
}
