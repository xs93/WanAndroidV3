package com.github.xs93.wan.common.viewmodel

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.IUiAction
import com.github.xs93.framework.base.viewmodel.IUiEvent
import com.github.xs93.framework.base.viewmodel.IUiState
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviEvents
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.wan.common.R
import com.github.xs93.wan.common.data.respotory.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 登录ViewModel
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
) : IUiState

sealed class LoginEvent : IUiEvent {
    data class LoginResultEvent(val success: Boolean, val errorMsg: String?) : LoginEvent()
}

sealed class LoginAction : IUiAction {
    data class ClickLoginAction(val account: String?, val password: String?) : LoginAction()

    data class AccountErrorEnableAction(val enable: Boolean) : LoginAction()

    data class PwdErrorEnableAction(val enable: Boolean) : LoginAction()
}

@HiltViewModel
class LoginViewModel @Inject constructor(private val accountRepository: AccountRepository) :
    BaseViewModel() {


    private val loginState by mviStates(LoginState())
    val loginStateFlow = loginState.flow

    private val loginEvent by mviEvents<LoginEvent>()
    val loginEventFlow = loginEvent.flow

    val loginAction by mviActions<LoginAction> {
        when (it) {
            is LoginAction.ClickLoginAction -> login(it.account, it.password)
            is LoginAction.AccountErrorEnableAction -> accountErrorEnable(it.enable, null)
            is LoginAction.PwdErrorEnableAction -> pwdErrorEnable(it.enable, null)
        }
    }


    private fun login(username: String?, password: String?) {
        launcher {
            if (username.isNullOrBlank()) {
                accountErrorEnable(true, getString(R.string.login_error_input_account))
                return@launcher
            }

            if (password.isNullOrBlank()) {
                pwdErrorEnable(true, getString(R.string.login_error_input_password))
                return@launcher
            }
            showLoadingDialog()
            val loginResult = accountRepository.login(username, password)
            loginResult.onSuccess {
                if (it.isSuccess()) {
                    // 更新用户详细信息
                    accountRepository.fetchUserInfo()
                    hideLoadingDialog()
                    loginEvent.send(LoginEvent.LoginResultEvent(true, null))
                } else {
                    hideLoadingDialog()
                    loginEvent.send(LoginEvent.LoginResultEvent(false, it.errorMessage))
                }
            }.onFailure {
                hideLoadingDialog()
                loginEvent.send(LoginEvent.LoginResultEvent(false, it.message))
            }
        }
    }

    private fun accountErrorEnable(enable: Boolean, errorMsg: String?) {
        loginState.update {
            copy(accountErrorEnable = enable, accountErrorMsg = errorMsg)
        }
    }

    private fun pwdErrorEnable(enable: Boolean, errorMsg: String?) {
        loginState.update {
            copy(pwdErrorEnable = enable, pwdErrorMsg = errorMsg)
        }
    }
}