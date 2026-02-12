package com.github.xs93.core.ktx

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 *
 * Activity Fragment 协程与生命周期扩展方法
 *
 * @author xushuai
 * @date   2022/9/8-23:01
 * @email  466911254@qq.com
 */

fun ViewModel.launcher(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return viewModelScope.launch(context, start) {
        block()
    }
}

fun ViewModel.launcherIO(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launcher(Dispatchers.IO, start, block)
}

fun ComponentActivity.launcher(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return lifecycleScope.launch(context, start, block)
}

fun ComponentActivity.repeatOnLifecycle(
    state: Lifecycle.State,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {

    return launcher(context, start) {
        repeatOnLifecycle(state, block)
    }
}

fun ComponentActivity.repeatOnStarted(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return repeatOnLifecycle(Lifecycle.State.STARTED, context, start, block)
}


fun Fragment.launcher(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return viewLifecycleOwner.lifecycleScope.launch(context, start, block)
}

fun Fragment.repeatOnLifecycle(
    state: Lifecycle.State,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return launcher(context, start) {
        repeatOnLifecycle(state, block)
    }
}

fun Fragment.repeatOnStarted(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return repeatOnLifecycle(Lifecycle.State.STARTED, context, start, block)
}

fun <R> ComponentActivity.observerState(
    flow: Flow<R>,
    areEquivalent: ((old: R, new: R) -> Boolean)? = null,
    action: suspend (R) -> Unit
) {
    val tempFlow = flow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
    if (areEquivalent != null) {
        tempFlow.distinctUntilChanged(areEquivalent)
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { action.invoke(it) }
            .launchIn(lifecycleScope)
    } else {
        tempFlow.distinctUntilChanged()
            .onEach {
                action.invoke(it)
            }
            .launchIn(lifecycleScope)
    }
}

fun <R> Fragment.observerState(
    flow: Flow<R>,
    areEquivalent: ((old: R, new: R) -> Boolean)? = null,
    action: suspend (R) -> Unit
) {
    val tempFlow = flow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
    if (areEquivalent != null) {
        tempFlow
            .distinctUntilChanged(areEquivalent)
            .onEach { action.invoke(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    } else {
        tempFlow.distinctUntilChanged()
            .onEach { action.invoke(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}

fun <R> ComponentActivity.observerEvent(flow: Flow<R>, action: suspend (R) -> Unit) {
    flow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
        .onEach {
            action.invoke(it)
        }
        .launchIn(lifecycleScope)
}

fun <R> Fragment.observerEvent(flow: Flow<R>, action: suspend (R) -> Unit) {
    flow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
        .onEach {
            action.invoke(it)
        }
        .launchIn(viewLifecycleOwner.lifecycleScope)
}