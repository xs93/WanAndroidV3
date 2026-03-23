package com.github.xs93.wan.bus

import com.github.xs93.core.bus.FlowBus
import com.github.xs93.wan.bus.event.CollectEvent

/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/3/23 9:42
 * @description FlowBus事件总线帮助类
 * 
 */
object BusHelper {

    /**
     * 收藏事件总线
     */
    val collectEventBus: FlowBus.FlowEventBus<CollectEvent>
        get() = FlowBus.with(BusKeyConstant.COLLECT_EVENT)
}