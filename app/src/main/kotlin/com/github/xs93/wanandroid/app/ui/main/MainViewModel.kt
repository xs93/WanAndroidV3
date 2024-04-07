package com.github.xs93.wanandroid.app.ui.main

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviEvents
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.wanandroid.common.services.impl.AccountServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 首页ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/7 13:54
 * @email 466911254@qq.com
 */

sealed class MainEvent : IUiEvent {
    data object OpenDrawerEvent : MainEvent()
}

sealed class MainAction : IUiAction {
    data object OpenDrawerAction : MainAction()

    data object LogoutAction : MainAction()
}

@HiltViewModel
class MainViewModel @Inject constructor(private val accountService: AccountServiceImpl) : BaseViewModel() {

    private val mainEvents by mviEvents<MainEvent>()
    val mainEventFlow = mainEvents.uiEventFlow


    val mainActions by mviActions<MainAction> {
        when (it) {
            MainAction.OpenDrawerAction -> openDrawer()
            MainAction.LogoutAction -> logout()
        }
    }

    private fun openDrawer() {
        mainEvents.sendEvent(MainEvent.OpenDrawerEvent)
    }

    private fun logout() {
        launcher {
            accountService.logout()
        }
    }
}