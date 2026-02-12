@file:Suppress("unused", "UNCHECKED_CAST")

package com.github.xs93.core.bus

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

/**
 * flow实现的事件总线
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/29 11:25
 * @email 466911254@qq.com
 */
object FlowBus {

    private const val TAG = "FlowBus"
    private val busMap = ConcurrentHashMap<String, FlowEventBus<*>>()
    private val stickyBusMap = ConcurrentHashMap<String, FlowStickyEventBus<*>>()
    private val defaultScope by lazy { MainScope() }

    @Synchronized
    fun <T> with(key: String): FlowEventBus<T> {
        return busMap.getOrPut(key) { FlowEventBus<T>(key) } as FlowEventBus<T>
    }

    @Synchronized
    fun <T> withSticky(key: String): FlowStickyEventBus<T> {
        return stickyBusMap.getOrPut(key) { FlowStickyEventBus<T>(key) } as FlowStickyEventBus<T>
    }

    fun <T> post(key: String, event: T) {
        post(defaultScope, key, event)
    }

    fun <T> post(scope: CoroutineScope, key: String, event: T) {
        with<T>(key).post(scope, event)
    }

    suspend fun <T> postSuspend(key: String, event: T) {
        with<T>(key).post(event)
    }

    fun <T> postSticky(key: String, event: T) {
        postSticky(defaultScope, key, event)
    }

    fun <T> postSticky(scope: CoroutineScope, key: String, event: T) {
        withSticky<T>(key).post(scope, event)
    }

    suspend fun <T> postStickySuspend(key: String, event: T) {
        withSticky<T>(key).post(event)
    }

    fun <T> observer(key: String, lifecycleOwner: LifecycleOwner, action: (t: T) -> Unit) {
        with<T>(key).subscribe(lifecycleOwner, action)
    }

    fun <T> observerSticky(key: String, lifecycleOwner: LifecycleOwner, action: (t: T) -> Unit) {
        withSticky<T>(key).subscribe(lifecycleOwner, action)
    }

    suspend fun <T> observer(key: String, action: (t: T) -> Unit) {
        with<T>(key).observer(action)
    }

    suspend fun <T> observerSticky(key: String, action: (t: T) -> Unit) {
        withSticky<T>(key).observer(action)
    }

    open class FlowEventBus<T>(private val key: String) : LifecycleEventObserver {
        private val _events: MutableSharedFlow<T> by lazy {
            obtainEvent()
        }

        open fun obtainEvent(): MutableSharedFlow<T> =
            MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                val subscribeCount = _events.subscriptionCount.value
                if (subscribeCount <= 0) {
                    busMap.remove(key)
                }
            }
        }

        /**
         * 订阅消息,和生命周期绑定,无需关心生命周期泄漏
         * @param lifecycleOwner LifecycleOwner
         * @param action Function1<T, Unit> 消息处理事件
         */
        fun subscribe(lifecycleOwner: LifecycleOwner, action: (t: T) -> Unit) {
            lifecycleOwner.lifecycle.addObserver(this)
            lifecycleOwner.lifecycleScope.launch {
                _events.collect {
                    try {
                        action(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(TAG, "subscribe: error", e)
                    }
                }
            }
        }

        /**
         * 协程中订阅事件,需要自己在合适的时候取消订阅
         * @param action (T) -> Unit 消息处理事件
         */
        suspend fun observer(action: (t: T) -> Unit) {
            _events.collect {
                try {
                    action(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "subscribe: error", e)
                }
            }
        }

        /**
         * 主线程中订阅事件,使用合适的 scope取消订阅
         */
        fun observer(scope: CoroutineScope, action: (t: T) -> Unit) {
            scope.launch {
                observer(action)
            }
        }

        /**
         * 协程中发送消息
         * @param value T 消息值
         */
        suspend fun post(value: T) {
            _events.emit(value)
        }

        /**
         * 主线程中发送消息
         * @param value T 消息值
         * @param scope CoroutineScope 协程scope
         */
        fun post(scope: CoroutineScope, value: T) {
            scope.launch {
                post(value)
            }
        }

        fun destroy() {
            Log.w(TAG, "destroy: 手动销毁")
            val subscribeCount = _events.subscriptionCount.value
            if (subscribeCount <= 0) {
                busMap.remove(key)
            }
        }
    }

    /**
     * 粘性事件
     */
    class FlowStickyEventBus<T>(key: String) : FlowEventBus<T>(key) {

        override fun obtainEvent(): MutableSharedFlow<T> =
            MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
    }
}