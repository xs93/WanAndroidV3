package com.github.xs93.framework.base.ui.function

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * activity生命周期功能
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/5 10:03
 */
open class BaseActivityFunction {

    open fun attacheBaseContext(newBase: Context): Context {
        return newBase
    }

    open fun onNewIntent(activity: Activity, intent: Intent?) {

    }

    open fun onCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    open fun setupEnableEdgeToEdge(activity: ComponentActivity): Boolean {
        return false
    }


    open fun onRestarted(activity: Activity) {

    }

    open fun onStarted(activity: Activity) {

    }

    open fun onResumed(activity: Activity) {

    }

    open fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

    }

    open fun onPaused(activity: Activity) {

    }

    open fun onStopped(activity: Activity) {

    }

    open fun onSaveInstanceState(activity: Activity, outState: Bundle) {

    }

    open fun onRestoreInstanceState(activity: Activity, savedInstanceState: Bundle) {

    }

    open fun onDestroyed(activity: Activity) {

    }

    open fun onFinish(activity: Activity) {

    }

    open fun onWindowFocusChanged(activity: Activity, hasFocus: Boolean) {

    }

    /**
     * 转换getResource 结果
     * @param resources Resources 原getResource()结果值
     * @return Resources 转换后的结果值
     */
    open fun convertGetResourceResult(activity: Activity, resources: Resources) {

    }
}