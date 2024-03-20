package com.github.xs93.wanandroid.app.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.github.xs93.framework.base.ui.base.BaseActivity
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.wanandroid.app.router.AppNavGraph
import com.github.xs93.wanandroid.app.ui.theme.AppTheme
import kotlinx.coroutines.delay

class MainActivity : BaseActivity() {

    private lateinit var splashScreen: SplashScreen
    private var keepOnScreenCondition = true

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            keepOnScreenCondition
        }
        launcher {
            delay(1500L)
            keepOnScreenCondition = false
        }
        setContent {
            AppTheme {
                AppNavGraph()
            }
        }
    }
}