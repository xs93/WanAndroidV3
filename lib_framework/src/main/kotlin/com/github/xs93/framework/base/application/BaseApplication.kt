package com.github.xs93.framework.base.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.github.xs93.framework.activity.ActivityStackManager
import com.github.xs93.framework.crash.CrashHandler
import com.github.xs93.framework.loading.LoadingDialogHelper
import com.github.xs93.framework.loading.impl.DefaultCreateLoadingDialog

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

    /* 当前activity对象的数量 */
    private var mCurrentActivitySize = 0

    /* 当前App是否在后台 */
    private var mAppBackground = false

    /* app回到后台的的时间 */
    private var mAppToBackgroundTime = 0L

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        addComponentApplication(mComponentAppClassNameList)
        mHelper.initAppObjects(mComponentAppClassNameList)
        mHelper.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        com.github.xs93.utils.AppInject.init(this)
        LoadingDialogHelper.initLoadingDialog(DefaultCreateLoadingDialog())
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

    abstract fun addComponentApplication(classNames: MutableList<String>)


    private fun registerAppLifecycleListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                if (mCurrentActivitySize <= 0 && mAppBackground) {
                    appBackForeground(activity, System.currentTimeMillis() - mAppToBackgroundTime)
                }
                mCurrentActivitySize++
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
                mCurrentActivitySize--
                if (mCurrentActivitySize <= 0) {
                    mAppBackground = true
                    mAppToBackgroundTime = System.currentTimeMillis()
                    appBackBackground(activity)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }


    open fun appBackForeground(activity: Activity, backgroundTime: Long) {

    }

    open fun appBackBackground(activity: Activity) {

    }
}