package com.github.xs93.wanandroid

import com.github.xs93.framework.base.application.BaseApplication

/**
 * 公共Application 初始化
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/1/17 9:43
 * @email 466911254@qq.com
 */
open class CommonApplication : BaseApplication() {

    private val appComponent = CommonAppLifecycle()
    override fun onCreate() {
        super.onCreate()
        appComponent.onCreate(this)
    }
}