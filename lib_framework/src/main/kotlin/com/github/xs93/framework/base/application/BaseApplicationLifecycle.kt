package com.github.xs93.framework.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.github.xs93.framework.activity.ActivityStackManager
import com.github.xs93.framework.loading.LoadingDialogHelper
import com.github.xs93.framework.loading.impl.DefaultCreateLoadingDialog
import com.github.xs93.utils.AppInject

/**
 * 基础初始化封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/18 11:06
 * @email 466911254@qq.com
 */
abstract class BaseApplicationLifecycle : IAppLifecycle {

    private val mComponentAppClassNameList = mutableListOf<String>()
    private val mHelper = AppLifecycleHelper()

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        addComponentApplication(mComponentAppClassNameList)
        mHelper.initAppObjects(mComponentAppClassNameList)
        mHelper.attachBaseContext(context)
    }

    override fun onCreate(application: Application) {
        AppInject.init(application)
        LoadingDialogHelper.initLoadingDialog(DefaultCreateLoadingDialog())
        ActivityStackManager.init(application)
        registerAppLifecycleListener()
        mHelper.onCreate(application)
    }

    override fun onTerminate(application: Application) {
        super.onTerminate(application)
        mHelper.onTerminate(application)
    }

    override fun onLowMemory(application: Application) {
        super.onLowMemory(application)
        mHelper.onLowMemory(application)
    }

    override fun onTrimMemory(application: Application, level: Int) {
        super.onTrimMemory(application, level)
        mHelper.onTrimMemory(application, level)
    }


    override fun onConfigurationChanged(application: Application, newConfig: Configuration) {
        super.onConfigurationChanged(application, newConfig)
        mHelper.onConfigurationChanged(application, newConfig)
    }


    abstract fun addComponentApplication(classNames: MutableList<String>)

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