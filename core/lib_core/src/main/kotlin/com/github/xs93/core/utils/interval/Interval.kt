@file:Suppress("unused")

package com.github.xs93.core.utils.interval

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import java.io.Closeable
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * 轮询计时器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2025/2/21 10:25
 * @email 466911254@qq.com
 */
open class Interval @JvmOverloads constructor(
    var end: Long,
    private val period: Long,
    private val unit: TimeUnit,
    private val start: Long = 0,
    private val initialDelay: Long = 0,
) : Serializable, Closeable {

    /**
     * 创建一个不会自动结束的轮询器/计时器
     */
    @JvmOverloads
    constructor(
        period: Long,
        unit: TimeUnit,
        initialDelay: Long = 0L,
    ) : this(-1L, period, unit, 0L, initialDelay)

    private val subscribers = mutableListOf<Interval.(Long) -> Unit>()
    private val finishers = mutableListOf<Interval.(Long) -> Unit>()
    private var countTime = 0L
    private var delay = 0L
    private var scope: CoroutineScope? = null
    private lateinit var ticker: ReceiveChannel<Unit>

    /** 轮询器计数*/
    var count = start

    /** 轮询器当前状态*/
    var state = IntervalStatus.STATE_IDLE
        private set

    //<editor-fold desc="回调注册">
    /**
     * 订阅轮询器
     * 每次轮询器计时都会回调该回调函数
     * 轮询器完成时会同时触发轮询器完成回调
     */
    fun subscribe(action: Interval.(Long) -> Unit) = apply {
        subscribers.add(action)
    }

    /**
     * 轮询器完成回调
     * @see stop 执行该函数也会回调finish
     * @see cancel 执行该函数取消轮询器不会回调finish
     */
    fun finish(action: Interval.(Long) -> Unit) = apply {
        finishers.add(action)
    }
    //</editor-fold>

    //<editor-fold desc="操作">
    /**
     * 开始
     * 如果当前为暂停状态则会重新开始
     */
    fun start() = apply {
        if (state == IntervalStatus.STATE_ACTIVE) return this
        state = IntervalStatus.STATE_ACTIVE
        count = start
        launch()
    }

    /**
     * 停止轮询器
     */
    fun stop() {
        if (state == IntervalStatus.STATE_IDLE) return
        scope?.cancel()
        state = IntervalStatus.STATE_IDLE
        finishers.forEach {
            it.invoke(this, countTime)
        }
    }


    /**
     * 取消轮询器
     * 轮询器完成时不会回调finish
     */
    fun cancel() {
        if (state == IntervalStatus.STATE_IDLE) return
        scope?.cancel()
        state = IntervalStatus.STATE_IDLE
    }

    /**
     * 等于[cancel]
     * @see cancel 取消轮询器
     */
    override fun close() = cancel()

    /**
     * 暂停轮询器
     */
    fun pause() {
        if (state != IntervalStatus.STATE_ACTIVE) return
        scope?.cancel()
        state = IntervalStatus.STATE_PAUSE
        // 一个计时单位的总时间减去距离上次计时已过时间，等于resume 需要delay的时间
        delay = max(unit.toMillis(period) - (SystemClock.elapsedRealtime() - countTime), 0L)
    }

    /**
     * 恢复轮询器
     */
    fun resume() {
        if (state != IntervalStatus.STATE_PAUSE) return
        state = IntervalStatus.STATE_ACTIVE
        launch(delay)
    }

    /**
     * 重置轮询器
     */
    fun reset() {
        scope?.cancel()
        count = start
        delay = unit.toMillis(initialDelay)
        if (state == IntervalStatus.STATE_ACTIVE) launch()
    }


    /**
     * 切换轮询器状态
     * 如果轮询器处于暂停状态则恢复，处于计时状态则暂停
     * @see pause 暂停轮询器
     * @see start 开始轮询器
     * @see pause 暂停轮询器
     * @see resume 取消轮询器
     */
    fun switch() {
        when (state) {
            IntervalStatus.STATE_IDLE -> start()
            IntervalStatus.STATE_ACTIVE -> pause()
            IntervalStatus.STATE_PAUSE -> resume()
        }
    }
    //</editor-fold>

    //<editor-fold desc="生命周期管理">
    /**
     * 轮询器生命周期管理，在指定生命周期后取消轮询器
     * @param lifecycleOwner 生命周期拥有者,一般为 activity/fragment
     * @param lifeEvent 生命周期事件，默认为[Lifecycle.Event.ON_DESTROY]时停止轮询器
     */
    @JvmOverloads
    fun life(
        lifecycleOwner: LifecycleOwner,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    ) = apply {
        runMain {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == lifeEvent) {
                        cancel()
                    }
                }
            })
        }
    }

    /**
     * 轮询器生命周期管理，在指定生命周期后取消轮询器
     * @param lifeEvent 生命周期事件，默认为[Lifecycle.Event.ON_DESTROY]时停止轮询器
     */
    @JvmOverloads
    fun life(
        fragment: Fragment,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    ) = apply {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) {
            it?.lifecycle?.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == lifeEvent) {
                        cancel()
                    }
                }
            })
        }
    }

    /**
     * 轮询器生命周期管理，和ViewModel生命周期关联，当ViewModel销毁时取消轮询器
     */
    fun life(viewModel: ViewModel) = apply {
        viewModel.addCloseable(this)
    }

    /**
     * 当界面不可见时[pause],当界面可见时[resume],当界面销毁时[cancel]
     */
    fun onlyResumed(lifecycleOwner: LifecycleOwner) = apply {
        runMain {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> resume()
                        Lifecycle.Event.ON_PAUSE -> pause()
                        Lifecycle.Event.ON_DESTROY -> cancel()
                        else -> {}
                    }
                }
            })
        }
    }
    //</editor-fold>

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun launch(delay: Long = unit.toMillis(initialDelay)) {
        scope = CoroutineScope(Dispatchers.Main)
        scope?.launch {
            ticker = ticker(unit.toMillis(period), delay, mode = TickerMode.FIXED_DELAY)
            for (unit in ticker) {
                subscribers.forEach {
                    it.invoke(this@Interval, count)
                }
                if (end != -1L && count == end) {
                    scope?.cancel()
                    state = IntervalStatus.STATE_IDLE
                    finishers.forEach {
                        it.invoke(this@Interval, count)
                    }
                } else {
                    if (end != -1L && start > end) count-- else count++
                }
                countTime = SystemClock.elapsedRealtime()
            }
        }
    }

    private fun runMain(block: () -> Unit) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            block()
        } else {
            Handler(Looper.getMainLooper()).post {
                block()
            }
        }
    }
}