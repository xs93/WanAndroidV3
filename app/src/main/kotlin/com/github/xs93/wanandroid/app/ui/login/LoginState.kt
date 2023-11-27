package com.github.xs93.wanandroid.app.ui.login

import com.github.xs93.framework.base.viewmodel.IUIState
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/7 15:40
 * @email 466911254@qq.com
 */


data class LoginState(
    val accountErrorEnable: Boolean = false,
    val accountErrorMsg: String? = null,
    val pwdErrorEnable: Boolean = false,
    val pwdErrorMsg: String? = null
) : IUIState

sealed class LoginEvent : IUiEvent {
    data class LoginResultEvent(val success: Boolean, val errorMsg: String?) : LoginEvent()
}

sealed class LoginAction : IUiAction {
    data class ClickLoginAction(val account: String?, val password: String?) : LoginAction()

    data class AccountErrorEnableAction(val enable: Boolean) : LoginAction()

    data class PwdErrorEnableAction(val enable: Boolean) : LoginAction()
}