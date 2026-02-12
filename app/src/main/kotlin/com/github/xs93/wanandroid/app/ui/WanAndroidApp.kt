package com.github.xs93.wanandroid.app.ui

import com.github.xs93.core.base.app.BaseApplication
import dagger.hilt.android.HiltAndroidApp

/**
 * Application 实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/8/17 10:11
 * @email 466911254@qq.com
 */
@HiltAndroidApp
class WanAndroidApp : BaseApplication() {

    override fun addAppLifecycle(classNames: MutableList<String>) {
        classNames.add("com.github.xs93.wan.common.CommonAppLifecycle")
    }
}