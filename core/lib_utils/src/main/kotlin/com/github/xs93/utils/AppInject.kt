package com.github.xs93.utils

import android.app.Application
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.MainScope

/**
 * Application 全部保存，生命周期和 app运行一致
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/1/27 16:22
 */
object AppInject {

    private var mApplication: Application? = null

    /**
     * 全局的协程Scope
     */
    val mainScope by lazy {
        MainScope()
    }

    /**
     *   全局的Handler,不要在里面使用UI相关的操作
     */
    val appMainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

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