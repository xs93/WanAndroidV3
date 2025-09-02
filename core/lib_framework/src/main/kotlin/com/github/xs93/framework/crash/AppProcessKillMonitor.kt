package com.github.xs93.framework.crash

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import kotlin.system.exitProcess

/**
 *
 * 当后台设置重置权限,导致进程被杀死新建，单列对象的数据状态错误,引起崩溃或者其他错误
 * 当监控到App进程被杀死新建,则重启APP
 * 新建以后 isProcessKilled会被重置为true,可以判断进程被杀死了
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/11/20 13:12
 * @email 466911254@qq.com
 */
object AppProcessKillMonitor : Application.ActivityLifecycleCallbacks {

    private var isProcessKilled: Boolean = true
    private var application: Application? = null
    private var activityCount = 0

    private val excludeActivities = mutableListOf<String>()

    fun register(app: Application) {
        this.application = app
        app.registerActivityLifecycleCallbacks(this)
    }

    /**
     * 添加需要排除的Activity,比如通知栏点击启动时直接打开目标activity时,新建进程不能判断为进程被杀死
     */
    fun addExcludeActivity(activityName: String) {
        excludeActivities.add(activityName)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (isLaunchActivity(activity) || isExcludeActivity(activity)) {
            if (activityCount == 0 || activity.isTaskRoot) {
                isProcessKilled = false
            }
        }
        if (isProcessKilled) { // 进程被杀死重启,重新启动APP
            restartApp(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        activityCount++
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }


    private fun isLaunchActivity(activity: Activity): Boolean {
        val packageManager = activity.packageManager
        val packageName = activity.packageName
        val launcherIntent = packageManager.getLaunchIntentForPackage(packageName)
        val launcherActivityName = launcherIntent?.component?.className
        return launcherActivityName == activity.javaClass.name
    }

    private fun isExcludeActivity(activity: Activity): Boolean {
        return excludeActivities.contains(activity.javaClass.name)
    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val launcherIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launcherIntent != null) {
            launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(launcherIntent)
        }
        // 杀死当前进程
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}