package com.github.xs93.wanandroid.main

import com.alibaba.android.arouter.launcher.ARouter
import com.github.xs93.core.base.viewmodel.BaseViewModel
import com.github.xs93.core.ktx.launcher
import com.github.xs93.wanandroid.common.router.RouterPath
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/9/27 15:29
 * @email 466911254@qq.com
 */
class MainViewModel : BaseViewModel() {

    private val _stateFlow = MutableStateFlow(MainState(false))
    val stateFlow: StateFlow<MainState> = _stateFlow

    private val _eventFlow = MutableSharedFlow<MainEvent>()
    val eventFlow: SharedFlow<MainEvent> = _eventFlow

    fun input(intent: MainIntent) {
        when (intent) {
            is MainIntent.ClickLoginAction -> {
                ARouter.getInstance().build(RouterPath.Login.LoginActivity).navigation()
            }
            is MainIntent.CloseDrawerAction -> {
                launcher {
                    _eventFlow.emit(MainEvent.CloseDrawerEvent)
                }
            }
            is MainIntent.OpenDrawerAction -> {
                launcher {
                    _eventFlow.emit(MainEvent.OpenDrawerEvent)
                }
            }
            is MainIntent.LoginEventAction -> {
                launcher {
                    _stateFlow.emit(_stateFlow.value.copy(login = intent.login))
                }
            }
        }
    }
}