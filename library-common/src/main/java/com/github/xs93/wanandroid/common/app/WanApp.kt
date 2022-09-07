package com.github.xs93.wanandroid.common.app

import com.github.xs93.core.base.application.BaseApplication
import com.github.xs93.wanandroid.common.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * Application
 *
 *
 * @author xushuai
 * @date   2022/9/5-16:42
 * @email  466911254@qq.com
 */
class WanApp : BaseApplication() {
    override fun addComponentApplication(classNames: MutableList<String>) {

    }

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}