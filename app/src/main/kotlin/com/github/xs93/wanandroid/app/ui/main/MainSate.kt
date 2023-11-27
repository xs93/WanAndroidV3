package com.github.xs93.wanandroid.app.ui.main

import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent

/**
 * 主页Event
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/7 13:55
 * @email 466911254@qq.com
 */

sealed class MainEvent : IUiEvent {
    data object OpenDrawerEvent : MainEvent()
}

sealed class MainAction : IUiAction {
    data object OpenDrawerAction : MainAction()
}