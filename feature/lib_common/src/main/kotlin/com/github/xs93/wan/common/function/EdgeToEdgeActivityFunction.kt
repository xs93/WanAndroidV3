package com.github.xs93.wan.common.function

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import com.github.xs93.ui.base.ui.function.BaseActivityFunction

/**
 * 修改edgeToEdge调用方法
 *
 * @author XuShuai
 * @version v1.0
 * @date 2024/5/16 11:02
 * @email 466911254@qq.com
 */
class EdgeToEdgeActivityFunction : BaseActivityFunction() {

    override fun setupEnableEdgeToEdge(activity: ComponentActivity): Boolean {
        activity.enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT))
        return true
    }
}