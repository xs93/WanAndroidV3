package com.github.xs93.framework.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration

/**
 *
 * 组件Application辅助类
 *
 * @author xushuai
 * @date   2022/3/6-13:43
 * @email  466911254@qq.com
 */
internal class AppLifecycleHelper : IAppLifecycle {

    private val mAppLifecycleObjects = mutableListOf<IAppLifecycle>()

    fun initAppObjects(classNames: List<String>) {
        mAppLifecycleObjects.clear()
        for (className in classNames) {
            try {
                val clazz = Class.forName(className)
                val obj = clazz.getDeclaredConstructor().newInstance()
                if (obj is IAppLifecycle) {
                    mAppLifecycleObjects.add(obj)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun attachBaseContext(context: Context) {
        for (app in mAppLifecycleObjects) {
            app.attachBaseContext(context)
        }
    }

    override fun onCreate(application: Application) {
        for (app in mAppLifecycleObjects) {
            app.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        for (app in mAppLifecycleObjects) {
            app.onTerminate(application)
        }
    }

    override fun onLowMemory(application: Application) {
        for (app in mAppLifecycleObjects) {
            app.onLowMemory(application)
        }
    }

    override fun onTrimMemory(application: Application, level: Int) {
        for (app in mAppLifecycleObjects) {
            app.onTrimMemory(application, level)
        }
    }

    override fun onConfigurationChanged(application: Application, newConfig: Configuration) {
        for (app in mAppLifecycleObjects) {
            app.onConfigurationChanged(application, newConfig)
        }
    }
}