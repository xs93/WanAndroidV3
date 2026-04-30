package com.github.xs93.wan.common.service

import com.github.xs93.wan.model.event.MainDrawerOpEvent
import kotlinx.coroutines.flow.SharedFlow

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/4/30 17:32
 * @description 首页服务
 * 
 */
interface IMainService {

    /**
     * 侧边栏打开关闭事件
     */
    val drawerOpEventFlow: SharedFlow<MainDrawerOpEvent>

    /**
     * 打开侧边栏
     */
    fun openDrawer()
}