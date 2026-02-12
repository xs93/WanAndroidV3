package com.github.xs93.core.base.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.github.xs93.core.AppInject
import com.github.xs93.core.activity.ActivityStackManager

/**
 * 基础Application
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/5 9:58
 */
abstract class BaseApplication : Application() {

    private val mAppLifecycleClassNameList = mutableListOf<String>()
    private val mHelper = AppLifecycleHelper()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // 初始化AppInject，在这里就初始化,保证SP在attachBaseContext里面使用sp没有初始化问题
        AppInject.init(this)
        addAppLifecycle(mAppLifecycleClassNameList)
        mHelper.initAppObjects(mAppLifecycleClassNameList)
        mHelper.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppInject.init(this)
        ActivityStackManager.init(this)
        registerAppLifecycleListener()
        mHelper.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mHelper.onTerminate(this)
    }

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() {
        super.onLowMemory()
        mHelper.onLowMemory(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        mHelper.onTrimMemory(this, level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mHelper.onConfigurationChanged(this, newConfig)
    }

    open fun addAppLifecycle(classNames: MutableList<String>) {
    }

    private fun registerAppLifecycleListener() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                applicationLifecycleEventObserver(event)
            }
        })
    }

    open fun applicationLifecycleEventObserver(event: Lifecycle.Event) {

    }
}