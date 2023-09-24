package com.github.xs93.framework.bus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

/**
 * flow实现的事件总线
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/8/29 11:25
 * @email 466911254@qq.com
 */
object FlowBus {

    private val mEvents = ConcurrentHashMap<String, EventBus<*>>()
    private val mStickyEvents = ConcurrentHashMap<String, EventBus<*>>()

    @Suppress("UNCHECKED_CAST")
    @Synchronized
    fun <T> with(key: String): EventBus<T> {
        if (!mEvents.containsKey(key)) {
            mEvents[key] = EventBus<T>(key)
        }
        return mEvents[key] as EventBus<T>
    }

    @Suppress("UNCHECKED_CAST")
    @Synchronized
    fun <T> withSticky(key: String): StickyEventBus<T> {
        if (!mStickyEvents.containsKey(key)) {
            mStickyEvents[key] = StickyEventBus<T>(key)
        }
        return mStickyEvents[key] as StickyEventBus<T>
    }

    suspend fun <T> post(key: String, value: T) {
        with<T>(key).post(value)
    }

    fun <T> post(scope: CoroutineScope, key: String, value: T) {
        with<T>(key).post(scope, value)
    }

    suspend fun <T> postSticky(key: String, value: T) {
        withSticky<T>(key).post(value)
    }

    fun <T> postSticky(scope: CoroutineScope, key: String, value: T) {
        withSticky<T>(key).post(scope, value)
    }


    suspend fun <T> subscribe(key: String, action: (T) -> Unit) {
        with<T>(key).subscribe(action)
    }

    fun <T> subscribe(key: String, lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
        with<T>(key).subscribe(lifecycleOwner, action)
    }

    suspend fun <T> subscribeSticky(key: String, action: (T) -> Unit) {
        withSticky<T>(key).subscribe(action)
    }

    fun <T> subscribeSticky(key: String, lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
        withSticky<T>(key).subscribe(lifecycleOwner, action)
    }


    open class EventBus<T>(private val key: String) : LifecycleEventObserver {


        private val _eventFlow: MutableSharedFlow<T> by lazy {
            obtainEvent()
        }

        open fun obtainEvent(): MutableSharedFlow<T> =
            MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                val subscribeCount = _eventFlow.subscriptionCount.value
                if (subscribeCount <= 0) {
                    mEvents.remove(key)
                }
            }
        }

        /**
         * 订阅消息,和生命周期绑定,无需关心生命周期泄漏
         * @param lifecycleOwner LifecycleOwner
         * @param action Function1<T, Unit> 消息处理事件
         */
        fun subscribe(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) {
            lifecycleOwner.lifecycle.addObserver(this)
            lifecycleOwner.lifecycleScope.launch {
                _eventFlow.collect {
                    action(it)
                }
            }
        }

        /**
         * 协程中订阅事件,需要自己在合适的时候取消订阅
         * @param action (T) -> Unit 消息处理事件
         */
        suspend fun subscribe(action: (T) -> Unit) {
            _eventFlow.collect {
                action(it)
            }
        }

        /**
         * 协程中发送消息
         * @param value T 消息值
         */
        suspend fun post(value: T) {
            _eventFlow.emit(value)
        }

        /**
         * 主线程中发送消息
         * @param scope CoroutineScope 协程scope
         * @param value T 消息值
         */
        fun post(scope: CoroutineScope, value: T) {
            scope.launch {
                _eventFlow.emit(value)
            }
        }
    }

    class StickyEventBus<T>(key: String) : EventBus<T>(key) {

        override fun obtainEvent(): MutableSharedFlow<T> =
            MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
    }
}