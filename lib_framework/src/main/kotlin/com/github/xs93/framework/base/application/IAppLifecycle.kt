package com.github.xs93.framework.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 * 组件Application初始化接口,各个模块可以定义自己需要的初始化
 *
 *
 * @author xushuai
 * @date   2022/3/6-9:47
 * @email  466911254@qq.com
 */
interface IAppLifecycle {

    fun attachBaseContext(context: Context) {

    }

    fun onCreate(application: Application)

    fun onTerminate(application: Application) {

    }

    fun onLowMemory(application: Application) {

    }

    fun onTrimMemory(application: Application, level: Int) {

    }

    fun onConfigurationChanged(application: Application, newConfig: Configuration) {

    }
}