package com.github.xs93.wan.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.xs93.core.ktx.launcher
import com.github.xs93.core.ktx.startActivitySafe
import com.github.xs93.ui.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.wan.main.databinding.ActivityStartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/9/2
 * @description 启屏页
 *
 */
@AndroidEntryPoint
class StartActivity : BaseVBActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {

    private lateinit var splashScreen: SplashScreen
    private var keepOnScreenCondition = true

    override fun beforeSuperOnCreate(savedInstanceState: Bundle?) {
        super.beforeSuperOnCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            keepOnScreenCondition
        }
    }

    override fun beforeSetContentView(savedInstanceState: Bundle?) {
        super.beforeSetContentView(savedInstanceState)
        if ((intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0 && !isTaskRoot) {
            finish()
            return
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        launcher {
            if (savedInstanceState != null) {
                keepOnScreenCondition = false
            } else {
                delay(1000L)
                keepOnScreenCondition = false
            }
            startActivitySafe<MainActivity>()
            finish()
        }
    }
}