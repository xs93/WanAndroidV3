package com.github.xs93.wanandroid.login.login

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/9/27 16:28
 * @email 466911254@qq.com
 */
data class LoginViewState(
    val accountErrorEnable: Boolean = false,
    val accountErrorMsg: String = "",
    val pwdErrorEnable: Boolean = false,
    val pwdErrorMsg: String = ""
)

sealed class LoginViewEvent {
    data class ToastEvent(val msg: String) : LoginViewEvent()
    object ShowLoadingEvent : LoginViewEvent()
    object HideLoadingEvent : LoginViewEvent()
    object FinishEvent : LoginViewEvent()
}

sealed class LoginIntent {
    data class ClickLoginIntent(val account: String?, val password: String?) : LoginIntent()
    object RegisterAccountIntent : LoginIntent()
    data class AccountErrorEnableIntent(val enable: Boolean) : LoginIntent()
    data class PwdErrorEnableIntent(val enable: Boolean) : LoginIntent()
}

