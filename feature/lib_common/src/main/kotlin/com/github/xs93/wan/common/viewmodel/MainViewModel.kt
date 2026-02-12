package com.github.xs93.wan.common.viewmodel

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviEvents
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.wan.data.respotory.AccountRepository
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
class MainViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    BaseViewModel() {

    private val mainEvents by mviEvents<MainEvent>()
    val mainEventFlow = mainEvents.flow


    val mainActions by mviActions<MainAction> {
        when (it) {
            MainAction.OpenDrawerAction -> openDrawer()
            MainAction.LogoutAction -> logout()
        }
    }

    private fun openDrawer() {
        mainEvents.send(MainEvent.OpenDrawerEvent)
    }

    private fun logout() {
        launcher {
            accountRepository.logout()
        }
    }
}