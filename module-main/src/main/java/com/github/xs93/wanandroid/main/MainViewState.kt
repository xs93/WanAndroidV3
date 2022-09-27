package com.github.xs93.wanandroid.main

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/9/27 15:22
 * @email 466911254@qq.com
 */

data class MainState(
    val login: Boolean = false
)

sealed class MainEvent {
    data class ToastEvent(val msg: String) : MainEvent()
    object OpenDrawerEvent : MainEvent()
    object CloseDrawerEvent : MainEvent()
}

sealed class MainIntent {
    object ClickLoginAction : MainIntent()
    object OpenDrawerAction : MainIntent()
    object CloseDrawerAction : MainIntent()
    data class LoginEventAction(val login: Boolean) : MainIntent()
}

