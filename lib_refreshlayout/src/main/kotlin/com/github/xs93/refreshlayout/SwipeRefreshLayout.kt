package com.github.xs93.refreshlayout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.absoluteValue

/**
 * 下拉刷新
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/3/20 15:06
 * @email 466911254@qq.com
 */

@Composable
fun SwipeRefreshLayout(
    modifier: Modifier,
    state: SwipeRefreshState,
    style: SwipeRefreshStyle = SwipeRefreshStyle.Translate,
    onRefresh: () -> Unit = {},
    swipeEnable: () -> Boolean = { true },
    refreshTriggerRate: Float = 0.5f,////刷新生效高度与indicator高度的比例
    maxDragRate: Float = 1f,// 最大刷新距离与indicator高度的比例,
    indicator: @Composable (state: SwipeRefreshState) -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var indicatorHeight by remember { mutableIntStateOf(0) }
    val refreshTrigger = indicatorHeight * refreshTriggerRate
    val maxDrag = indicatorHeight * maxDragRate

    val nestedScrollConnection = remember(state, coroutineScope) {
        SwipeRefreshNestedScrollConnection(state, coroutineScope)
    }.apply {
        this.enable = swipeEnable.invoke()
        this.maxDrag = maxDrag
        this.refreshTrigger = refreshTrigger
    }

    LaunchedEffect(state.refreshStateFlag) {
        when (state.refreshStateFlag) {
            SwipeRefreshStateFlag.REFRESHING -> {
                onRefresh.invoke()
                state.animateToOffset(maxDrag)
                state.refreshAnimateFinishing = SwipeRefreshAnimateFinishing(isFinishing = false, isRefresh = true)
            }

            SwipeRefreshStateFlag.SUCCESS, SwipeRefreshStateFlag.ERROR -> {
                delay(1000L)
                state.animateToOffset(0f)
            }

            else -> {}
        }
    }

    Box(
        modifier = modifier.nestedScroll(nestedScrollConnection)
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned {
                indicatorHeight = it.size.height
            }
            .let { if (isNeedClip(state, indicatorHeight)) it.clipToBounds() else it }
            .offset {
                getHeaderOffset(style, state, indicatorHeight)
            }
            .zIndex(getHeaderZIndex(style))) {
            indicator(state)
        }
        Box(modifier = Modifier.offset {
            getContentOffset(style, state)
        }) {
            content()
        }
    }
}

private fun isNeedClip(state: SwipeRefreshState, indicatorHeight: Int): Boolean {
    return state.indicatorOffset < indicatorHeight
}

private fun getHeaderOffset(style: SwipeRefreshStyle, state: SwipeRefreshState, indicatorHeight: Int): IntOffset {
    return when (style) {
        SwipeRefreshStyle.Translate -> {
            IntOffset(0, state.indicatorOffset.toInt() - indicatorHeight)
        }

        SwipeRefreshStyle.FixedBehind, SwipeRefreshStyle.FixedFront -> {
            IntOffset(0, 0)
        }

        else -> {
            IntOffset(0, state.indicatorOffset.toInt() - indicatorHeight)
        }
    }
}

private fun getHeaderZIndex(style: SwipeRefreshStyle): Float {
    return if (style == SwipeRefreshStyle.FixedFront || style == SwipeRefreshStyle.FixedContent) {
        1f
    } else {
        0f
    }
}

private fun getContentOffset(style: SwipeRefreshStyle, state: SwipeRefreshState): IntOffset {
    return when (style) {
        SwipeRefreshStyle.Translate -> {
            IntOffset(0, state.indicatorOffset.toInt())
        }

        SwipeRefreshStyle.FixedBehind -> {
            IntOffset(0, state.indicatorOffset.toInt())
        }

        else -> {
            IntOffset(0, 0)
        }
    }
}

@Composable
fun rememberSwipeRefreshState(): SwipeRefreshState {
    return remember {
        SwipeRefreshState()
    }
}

enum class SwipeRefreshStyle {
    Translate,// 平移，即内容与Header一起向下滑动，Translate为默认样式
    FixedBehind,////固定在背后，即内容向下滑动，Header不动
    FixedFront,// 固定在前面, 即Header固定在前，Header与Content都不滑动
    FixedContent////内容固定,Header向下滑动,即官方样式
}

enum class SwipeRefreshStateFlag {
    IDLE,// 默认状态
    PULL_DOWN,// 下拉未触发刷新状态
    RELEASE,// 释放触发刷新
    REFRESHING,// 刷新状态
    SUCCESS,// 刷新成功
    ERROR,// 失败状态
}

data class SwipeRefreshAnimateFinishing(val isFinishing: Boolean = true, val isRefresh: Boolean = true)

@Stable
class SwipeRefreshState {
    private val mutatorMutex = MutatorMutex()
    private val _indicatorOffset = Animatable(0f)
    val indicatorOffset: Float get() = _indicatorOffset.value

    var refreshStateFlag: SwipeRefreshStateFlag by mutableStateOf(SwipeRefreshStateFlag.IDLE)

    var refreshAnimateFinishing: SwipeRefreshAnimateFinishing by mutableStateOf(
        SwipeRefreshAnimateFinishing(isFinishing = true, isRefresh = true)
    )

    fun isRefreshing() = refreshStateFlag == SwipeRefreshStateFlag.REFRESHING || !refreshAnimateFinishing.isFinishing

    internal suspend fun animateToOffset(offset: Float) {
        mutatorMutex.mutate {
            _indicatorOffset.animateTo(offset, tween(300)) {
                if (this.value == 0f && !refreshAnimateFinishing.isFinishing) {
                    refreshAnimateFinishing = refreshAnimateFinishing.copy(isFinishing = true)
                }
                if (this.value == 0f) {
                    refreshStateFlag = SwipeRefreshStateFlag.IDLE
                }
            }
        }
    }

    internal suspend fun dispatchScrollDelta(delta: Float, maxDrag: Float, refreshTrigger: Float) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            val offset = minOf(_indicatorOffset.value + delta, maxDrag)
            _indicatorOffset.snapTo(offset)
            refreshStateFlag = if (offset > refreshTrigger) {
                SwipeRefreshStateFlag.RELEASE
            } else {
                SwipeRefreshStateFlag.PULL_DOWN
            }
        }
    }
}

private class SwipeRefreshNestedScrollConnection(val state: SwipeRefreshState, val coroutineScope: CoroutineScope) :
    NestedScrollConnection {

    private val dragMultiplier = 0.4f
    var enable: Boolean = false
    var maxDrag: Float = 0f
    var refreshTrigger: Float = 0f

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        return when {
            !enable -> Offset.Zero
            state.isRefreshing() -> Offset.Zero
            source == NestedScrollSource.Drag && available.y < 0 -> onScroll(available)
            else -> Offset.Zero
        }
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        return when {
            !enable -> Offset.Zero
            state.isRefreshing() -> Offset.Zero
            source == NestedScrollSource.Drag && available.y > 0 -> onScroll(available)
            else -> Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (!state.isRefreshing()) {
            if (state.indicatorOffset > refreshTrigger) {
                state.refreshStateFlag = SwipeRefreshStateFlag.REFRESHING
            } else {
                if (state.indicatorOffset != 0f) {
                    state.animateToOffset(0f)
                } else {
                    return super.onPreFling(available)
                }
            }
            return Velocity(available.x, available.y)
        }
        return super.onPreFling(available)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        return super.onPostFling(consumed, available)
    }

    private fun onScroll(available: Offset): Offset {
        val newOffset = (available.y * dragMultiplier + state.indicatorOffset).coerceAtLeast(0f)
        val dragConsumed = newOffset - state.indicatorOffset
        return if (dragConsumed.absoluteValue >= 0.5f) {
            coroutineScope.launch {
                state.dispatchScrollDelta(dragConsumed, maxDrag, refreshTrigger)
            }
            // Return the consumed Y
            Offset(x = 0f, y = dragConsumed / dragMultiplier)
        } else {
            Offset.Zero
        }
    }
}

@Composable
fun ClassSwipeRefreshHeader(flag: SwipeRefreshStateFlag, containerColor: Color = Color.Gray) {
    // 记录旧的flag,防止Success或者Error更新lastRecordTime无限更新
    var lastFlag by remember { mutableStateOf(SwipeRefreshStateFlag.IDLE) }
    var lastRecordTime by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(containerColor)
    ) {
        val refreshAnimate by rememberInfiniteTransition(label = "refreshAnimate").animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = InfiniteRepeatableSpec(tween(500, easing = LinearEasing)),
            label = "arrowRefreshAnimate"
        )
        val transitionState = remember {
            MutableTransitionState(0)
        }
        val transition = updateTransition(targetState = transitionState, label = "arrowTransition")
        val arrowDegrees by transition.animateFloat(transitionSpec = { tween(100) }, label = "arrowDegrees") {
            if (it.targetState == 0) 0f else 180f
        }
        transitionState.targetState = if (flag == SwipeRefreshStateFlag.RELEASE) 1 else 0
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.rotate(
                    if (flag == SwipeRefreshStateFlag.REFRESHING) {
                        refreshAnimate
                    } else {
                        arrowDegrees
                    }
                ),
                contentDescription = null,
                imageVector = when (flag) {
                    SwipeRefreshStateFlag.IDLE -> Icons.Default.KeyboardArrowDown
                    SwipeRefreshStateFlag.PULL_DOWN -> Icons.Default.KeyboardArrowDown
                    SwipeRefreshStateFlag.RELEASE -> Icons.Default.KeyboardArrowDown
                    SwipeRefreshStateFlag.REFRESHING -> Icons.Default.Refresh
                    SwipeRefreshStateFlag.SUCCESS -> {
                        if (lastFlag != flag) {
                            lastRecordTime = System.currentTimeMillis()
                        }
                        Icons.Default.Done
                    }

                    SwipeRefreshStateFlag.ERROR -> {
                        if (lastFlag != flag) {
                            lastRecordTime = System.currentTimeMillis()
                        }
                        Icons.Default.Warning
                    }
                }
            )
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(
                    text = when (flag) {
                        SwipeRefreshStateFlag.IDLE, SwipeRefreshStateFlag.PULL_DOWN -> "下拉刷新"
                        SwipeRefreshStateFlag.RELEASE -> "释放立即刷新"
                        SwipeRefreshStateFlag.REFRESHING -> "刷新中..."
                        SwipeRefreshStateFlag.SUCCESS -> "刷新成功"
                        SwipeRefreshStateFlag.ERROR -> "刷新失败"
                    }, fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "上次加载:${SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault()).format(lastRecordTime)}",
                    fontSize = 10.sp
                )
            }
        }
    }
    lastFlag = flag
}