package com.github.xs93.wanandroid

import com.github.xs93.framework.core.base.application.BaseApplication
import com.github.xs93.framework.core.ktx.isDebug
import com.github.xs93.framework.network.EasyRetrofit

/**
 * Application
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/5/19 17:06
 * @email 466911254@qq.com
 */
class WanAndroidApp : BaseApplication() {
    override fun addComponentApplication(classNames: MutableList<String>) {

    }

    override fun onCreate() {
        super.onCreate()
        EasyRetrofit.init(this, "https://www.wanandroid.com/", null, isDebug)
    }
}