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
internal class AppComponentHelper : IAppComponent {

    private val mAppComponentObjects = mutableListOf<IAppComponent>()

    fun initAppObjects(classNames: List<String>) {
        mAppComponentObjects.clear()
        for (className in classNames) {
            try {
                val clazz = Class.forName(className)
                val obj = clazz.getDeclaredConstructor().newInstance()
                if (obj is IAppComponent) {
                    mAppComponentObjects.add(obj)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun attachBaseContext(context: Context) {
        for (app in mAppComponentObjects) {
            app.attachBaseContext(context)
        }
    }

    override fun onCreate(application: Application) {
        for (app in mAppComponentObjects) {
            app.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        for (app in mAppComponentObjects) {
            app.onTerminate(application)
        }
    }

    override fun onLowMemory(application: Application) {
        for (app in mAppComponentObjects) {
            app.onLowMemory(application)
        }
    }

    override fun onTrimMemory(application: Application, level: Int) {
        for (app in mAppComponentObjects) {
            app.onTrimMemory(application, level)
        }
    }

    override fun onConfigurationChanged(application: Application, newConfig: Configuration) {
        for (app in mAppComponentObjects) {
            app.onConfigurationChanged(application, newConfig)
        }
    }
}