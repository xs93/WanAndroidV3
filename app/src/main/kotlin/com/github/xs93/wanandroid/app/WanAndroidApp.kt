package com.github.xs93.wanandroid.app

import com.github.xs93.wanandroid.WanAndroidBaseApp
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.HiltAndroidApp

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 10:11
 * @email 466911254@qq.com
 */
@HiltAndroidApp
class WanAndroidApp : WanAndroidBaseApp() {

    override fun onCreate() {
        super.onCreate()

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            BallPulseFooter(context)
        }
    }
}