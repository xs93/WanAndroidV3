package com.github.xs93.wanandroid.app.ui.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/3/12 17:41
 * @email 466911254@qq.com
 */

@Composable
fun ArticleDetailsScreen() {
    val activity = LocalContext.current as Activity
    val windowInsetsController = WindowCompat.getInsetsController(activity.window, LocalView.current)
    windowInsetsController.isAppearanceLightStatusBars = false
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Blue)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = com.github.xs93.common.R.drawable.common_ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(48.dp)
                            .padding(12.dp)
                    )
                    Text(
                        text = "WebView",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Red)
        ) {

        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewArticleDetails() {
    ArticleDetailsScreen()
}