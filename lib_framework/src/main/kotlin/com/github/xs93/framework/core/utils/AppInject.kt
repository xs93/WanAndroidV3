package com.github.xs93.framework.core.utils

import android.app.Application

/**
 * Application 全部保存，生命周期和 app运行一致
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/27 16:22
 */
object AppInject {

    private var mApplication: Application? = null

    @JvmStatic
    fun init(application: Application) {
        mApplication = application
    }

    @JvmStatic
    fun getApp(): Application {
        if (mApplication == null) {
            throw IllegalArgumentException("application is null,please call init() method")
        }
        return mApplication!!
    }
}