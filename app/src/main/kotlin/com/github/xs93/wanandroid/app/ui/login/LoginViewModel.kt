package com.github.xs93.wanandroid.app.ui.login

import com.github.xs93.framework.base.viewmodel.BaseViewModel
import com.github.xs93.framework.base.viewmodel.mviActions
import com.github.xs93.framework.base.viewmodel.mviEvents
import com.github.xs93.framework.base.viewmodel.mviStates
import com.github.xs93.framework.ktx.launcher
import com.github.xs93.network.base.viewmodel.safeRequestApi
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.repository.LoginRepository
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
@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var loginRepository: LoginRepository

    private val loginState by mviStates(LoginState())
    val loginStateFlow = loginState.uiStateFlow

    private val loginEvent by mviEvents<LoginEvent>()
    val loginEventFlow = loginEvent.uiEventFlow

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
            val repo = safeRequestApi {
                loginRepository.login(username, password)
            }
            hideLoadingDialog()
            if (repo?.isSuccess() == true) {
                loginEvent.sendEvent(LoginEvent.LoginResultEvent(true, null))
            } else {
                loginEvent.sendEvent(LoginEvent.LoginResultEvent(false, repo?.errorMessage))
            }
        }
    }

    private fun accountErrorEnable(enable: Boolean, errorMsg: String?) {
        loginState.updateState {
            copy(accountErrorEnable = enable, accountErrorMsg = errorMsg)
        }
    }

    private fun pwdErrorEnable(enable: Boolean, errorMsg: String?) {
        loginState.updateState {
            copy(pwdErrorEnable = enable, pwdErrorMsg = errorMsg)
        }
    }
}