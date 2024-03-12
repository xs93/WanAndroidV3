package com.github.xs93.wanandroid.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

/**
 *使用AndroidAutoSize的原理缩放density的原理适配
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/3/12 17:18
 * @email 466911254@qq.com
 */

@Composable
fun AutoSize(designWidthDp: Int, content: @Composable () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val density = LocalDensity.current.density
    val scaleDensity = remember(screenWidth, density) {
        val scale = screenWidth.toFloat() / designWidthDp.toFloat()
        val scaleDensity = density * scale
        Density(scaleDensity, 1f)
    }
    CompositionLocalProvider(LocalDensity.provides(scaleDensity)) {
        content()
    }
}