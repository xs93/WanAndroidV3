@file:OptIn(ExperimentalFoundationApi::class)

package com.github.xs93.wanandroid.app.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.github.xs93.framework.toast.ToastManager
import com.github.xs93.wanandroid.app.ui.theme.AppTheme
import com.github.xs93.wanandroid.app.ui.viewmodel.ExploreViewModel
import com.github.xs93.wanandroid.app.ui.widget.Banner

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/2/27 10:52
 * @email 466911254@qq.com
 */


@Composable
fun ExploreScreen(viewModel: ExploreViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        val uiState by viewModel.uiStateFlow.collectAsState()
        Banner(
            dataList = uiState.banners,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.CenterHorizontally)
                .aspectRatio(16 / 9f),
            autoLoop = true,
            onItemClick = {
                ToastManager.showToast(it.title)
            },
            indicatorModifier = Modifier.padding(start = 16.dp, bottom = 8.dp, end = 16.dp),
            indicatorAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model = it.imagePath,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewExploreScreen() {
    AppTheme {
        ExploreScreen()
    }
}