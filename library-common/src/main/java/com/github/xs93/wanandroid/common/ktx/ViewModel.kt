package com.github.xs93.wanandroid.common.ktx

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * ViewModel相关扩展
 *
 *
 * @author xushuai
 * @date   2022/9/7-9:28
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