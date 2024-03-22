package com.github.xs93.wanandroid.app.ui.widget

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.xs93.wanandroid.app.entity.BannerData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 14:11
 * @email 466911254@qq.com
 */


@ExperimentalFoundationApi
@Composable
fun Banner(
    dataList: List<BannerData>,
    modifier: Modifier = Modifier,
    timeMillis: Long = 3000,
    autoLoop: Boolean = true,
    onItemClick: ((data: BannerData) -> Unit)? = null,
    indicatorModifier: Modifier = Modifier,
    indicatorAlignment: Alignment = Alignment.BottomCenter,
    indicatorActiveColor: Color = MaterialTheme.colorScheme.primary,
    indicatorInactiveColor: Color = Color.Gray,
    indicatorActiveSize: Dp = 8.dp,
    indicatorInactiveSize: Dp = 6.dp,
    indicatorSpace: Dp = 6.dp,
    content: @Composable (data: BannerData) -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (dataList.isEmpty()) return@Box
        val pageSize = dataList.size
        val pageCount = 500
        val initialPage = (pageCount / 2) - (pageCount / 2) % pageSize
        val pageState = rememberPagerState(initialPage = initialPage) {
            pageCount
        }
        val scope = rememberCoroutineScope()

        var isAutoLoop by remember { mutableStateOf(autoLoop) }

        LaunchedEffect(key1 = pageState.currentPage, key2 = isAutoLoop) {
            if (pageState.pageCount > 0 && isAutoLoop) {
                delay(timeMillis)
                val nextPage = pageState.currentPage + 1
                // animateScrollToPage 方法会改变 pagerState.currentPage 触发重组
                // 重组时 LaunchedEffect key 变化，原来开启的协程被取消
                // 如果此时 animateScrollToPage 动画未执行完，pager 就会停在最后的位置
                // 所以 animateScrollToPage 需要在 rememberCoroutineScope 的协程中执行
                scope.launch {
                    pageState.animateScrollToPage(nextPage)
                }
            }
        }

        HorizontalPager(state = pageState,
            beyondBoundsPageCount = 2,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(pageState.currentPage) {
                    awaitEachGesture {
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            val touchEvent = event.changes.firstOrNull()
                            touchEvent?.let {
                                when {
                                    it.isConsumed -> return@awaitEachGesture
                                    it.changedToDownIgnoreConsumed() -> {
                                        isAutoLoop = false
                                    }

                                    it.changedToUpIgnoreConsumed() -> {
                                        isAutoLoop = autoLoop
                                    }
                                }
                            }
                        }
                    }
                }
                .clickable {
                    val curPage = pageState.currentPage % pageSize
                    val data = dataList[curPage]
                    onItemClick?.invoke(data)
                }

        ) { index ->
            val curPage = index % pageSize
            val data = dataList[curPage]
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                content.invoke(data)
            }
        }


        Box(
            modifier = Modifier
                .then(indicatorModifier)
                .align(indicatorAlignment)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in dataList.indices) {
                    val curPage = pageState.currentPage % pageSize
                    var size by remember { mutableStateOf(indicatorInactiveSize) }
                    size = if (curPage == i) indicatorActiveSize else indicatorInactiveSize
                    val color = if (curPage == i) indicatorActiveColor else indicatorInactiveColor
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color)
                            .animateContentSize()
                            .size(size)
                    )
                    if (i != dataList.lastIndex) {
                        Spacer(
                            modifier = Modifier
                                .height(0.dp)
                                .width(indicatorSpace)
                        )
                    }
                }
            }
        }

    }
}

fun Int.floorMod(other: Int) = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}