package com.chatbot.bkfire.club.compose.app.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/14 13:55
 * @email 466911254@qq.com
 */

@Composable
fun HomeScreen() {
    Text(text = "Home")
}


@Preview(showBackground = false, showSystemUi = false)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}