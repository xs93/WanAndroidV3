package com.github.xs93.wan.common.function

import android.app.Activity
import android.content.res.Resources
import com.github.xs93.framework.base.ui.function.BaseActivityFunction
import me.jessyan.autosize.AutoSizeCompat

/**
 * AutoSize 重写Activity的getResource方法,解决大部分适配问题
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/1/17 10:39
 * @email 466911254@qq.com
 */
class AutoSizeActivityFunction : BaseActivityFunction() {

    override fun convertGetResourceResult(activity: Activity, resources: Resources) {
        activity.runOnUiThread {
            AutoSizeCompat.autoConvertDensityOfGlobal(resources)
        }
    }
}