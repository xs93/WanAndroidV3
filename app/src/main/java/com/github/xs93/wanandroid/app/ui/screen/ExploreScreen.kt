package com.github.xs93.wanandroid.app.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.xs93.wanandroid.app.ui.theme.AppTheme
import com.github.xs93.wanandroid.app.ui.viewmodel.ExploreViewModel

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

    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewExploreScreen() {
    AppTheme {
        ExploreScreen()
    }
}