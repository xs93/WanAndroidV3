package com.github.xs93.framework.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.github.xs93.framework.activity.ActivityStackManager
import com.github.xs93.framework.crash.CrashHandler
import com.github.xs93.utils.AppInject

/**
 * 基础Application
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/5 9:58
 */
abstract class BaseApplication : Application() {

    private val mComponentAppClassNameList = mutableListOf<String>()
    private val mHelper = AppComponentHelper()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        addComponentApplication(mComponentAppClassNameList)
        mHelper.initAppObjects(mComponentAppClassNameList)
        mHelper.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppInject.init(this)
        CrashHandler.init(this)
        ActivityStackManager.init(this)
        registerAppLifecycleListener()
        mHelper.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mHelper.onTerminate(this)
    }

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

    open fun addComponentApplication(classNames: MutableList<String>) {

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