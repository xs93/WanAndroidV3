package com.github.xs93.wanandroid.login.login

import com.github.xs93.core.base.viewmodel.BaseViewModel
import com.github.xs93.core.bus.FlowBus
import com.github.xs93.core.utils.AppInject
import com.github.xs93.retrofit.model.RequestState
import com.github.xs93.wanandroid.common.bus.FlowBusKey
import com.github.xs93.wanandroid.common.ktx.launcher
import com.github.xs93.wanandroid.login.LoginRepository
import com.github.xs93.wanandroid.login.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class LoginViewModel : BaseViewModel() {

    private val loginRepository: LoginRepository by lazy {
        LoginRepository()
    }

    private val _stateFlow = MutableStateFlow(LoginViewState())
    val stateFlow: StateFlow<LoginViewState> = _stateFlow

    private val _eventChannel = Channel<LoginViewEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()


    fun input(intent: LoginIntent) {
        launcher {
            when (intent) {
                is LoginIntent.AccountErrorEnableIntent -> {
                    _stateFlow.emit(_stateFlow.value.copy(accountErrorEnable = intent.enable))
                }
                is LoginIntent.PwdErrorEnableIntent -> {
                    _stateFlow.emit(_stateFlow.value.copy(pwdErrorEnable = intent.enable))
                }
                is LoginIntent.ClickLoginIntent -> {
                    login(intent.account, intent.password)
                }
                LoginIntent.RegisterAccountIntent -> {

                }
            }
        }

    }


    private fun login(username: String?, password: String?) {
        launcher {
            if (username.isNullOrBlank()) {
                val msg = AppInject.getApp().getString(R.string.login_error_input_account)
                _stateFlow.emit(_stateFlow.value.copy(accountErrorEnable = true, accountErrorMsg = msg))
                return@launcher
            }
            if (password.isNullOrBlank()) {
                val msg = AppInject.getApp().getString(R.string.login_error_inout_password)
                _stateFlow.emit(_stateFlow.value.copy(pwdErrorEnable = true, pwdErrorMsg = msg))
                return@launcher
            }
            loginRepository.login(username, password)
                .collect {
                    when (it) {
                        is RequestState.Loading -> {
                            _eventChannel.send(LoginViewEvent.ShowLoadingEvent)
                        }
                        is RequestState.Error -> {
                            _eventChannel.send(LoginViewEvent.HideLoadingEvent)
                            _eventChannel.send(LoginViewEvent.ToastEvent("登录失败：${it.exception.message}"))
                        }
                        is RequestState.Success -> {
                            FlowBus.with<Boolean>(FlowBusKey.LOGIN_STATUS).post(true)
                            _eventChannel.send(LoginViewEvent.HideLoadingEvent)
                            _eventChannel.send(LoginViewEvent.ToastEvent("登录成功"))
                            _eventChannel.send(LoginViewEvent.FinishEvent)
                        }
                    }
                }
        }
    }
}