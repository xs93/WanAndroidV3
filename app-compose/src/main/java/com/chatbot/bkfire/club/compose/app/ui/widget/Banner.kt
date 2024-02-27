package com.chatbot.bkfire.club.compose.app.ui.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.chatbot.bkfire.club.compose.app.entity.BannerData
import kotlinx.coroutines.delay

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
    modifier: Modifier,
    timeMillis: Long = 3000,
    content: @Composable (data: BannerData) -> Unit,
    onClick: ((data: BannerData) -> Unit)? = null
) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (dataList.isEmpty()) {
            return@Box
        }
        val pageState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) {
            dataList.size
        }
        var executeChangePage by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = pageState.currentPage, key2 = executeChangePage) {
            if (pageState.pageCount > 0) {
                delay(timeMillis)
                pageState.animateScrollToPage((pageState.currentPage + 1) % (pageState.pageCount))
            }
        }

        HorizontalPager(state = pageState, modifier = Modifier.clickable {
            with(dataList[pageState.currentPage]) {
                onClick?.invoke(this)
            }
        }
        ) { page ->
            val data = dataList[page]
            content.invoke(data)
        }
    }
}