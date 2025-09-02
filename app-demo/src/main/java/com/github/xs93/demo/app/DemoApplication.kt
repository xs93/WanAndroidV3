package com.github.xs93.demo.app

import com.github.xs93.framework.base.application.BaseApplication
import com.github.xs93.framework.crash.CrashHandler

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/6/19 10:05
 * @description
 *
 */
class DemoApplication : BaseApplication() {


    private val crashHandler = CrashHandler()

    override fun onCreate() {
        super.onCreate()
        crashHandler.init(this)
    }
}