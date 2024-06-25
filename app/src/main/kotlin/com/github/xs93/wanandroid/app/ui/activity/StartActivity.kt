package com.github.xs93.wanandroid.app.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.utils.ktx.startActivitySafe
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.StartActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

/**
 * 启屏页
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/6/25 17:21
 * @email 466911254@qq.com
 */
@AndroidEntryPoint
class StartActivity :
    BaseViewBindingActivity<StartActivityBinding>(R.layout.start_activity, StartActivityBinding::bind) {

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