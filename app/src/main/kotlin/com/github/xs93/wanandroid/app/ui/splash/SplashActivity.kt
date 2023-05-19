package com.github.xs93.wanandroid.app.ui.splash

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.xs93.framework.core.base.ui.base.BaseActivity
import com.github.xs93.framework.core.base.ui.vbvm.BaseVbVmActivity
import com.github.xs93.framework.core.base.ui.viewbinding.BaseVbActivity
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.ActivitySplashBinding

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/19 17:29
 * @email 466911254@qq.com
 */
class SplashActivity : BaseVbActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun beforeSuperOnCreate(savedInstanceState: Bundle?) {
        super.beforeSuperOnCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
    }

    override fun initView(savedInstanceState: Bundle?) {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return false
                }
            }
        )
    }
}