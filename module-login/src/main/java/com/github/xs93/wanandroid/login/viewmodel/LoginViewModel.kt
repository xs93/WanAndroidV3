package com.github.xs93.wanandroid.login.viewmodel

import com.github.xs93.core.base.viewmodel.BaseViewModel
import com.github.xs93.core.bus.FlowBus
import com.github.xs93.core.utils.toast.ToastUtils
import com.github.xs93.retrofit.model.RequestState
import com.github.xs93.wanandroid.common.bus.FlowBusKey
import com.github.xs93.wanandroid.common.ktx.launcher
import com.github.xs93.wanandroid.login.LoginRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class LoginViewModel : BaseViewModel() {

    private val loginRepository: LoginRepository by lazy {
        LoginRepository()
    }
    private val mLoadingDialogFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val loadDialogFlow: SharedFlow<Boolean> = mLoadingDialogFlow

    fun login(username: String, password: String) {
        launcher {
            loginRepository.login(username, password)
                .collect {
                    when (it) {
                        is RequestState.Loading -> {
                            showLoading()
                        }
                        is RequestState.Error -> {
                            hideLoading()
                            ToastUtils.show("登录失败：${it.exception.message}")
                        }
                        is RequestState.Success -> {
                            FlowBus.with<Boolean>(FlowBusKey.LOGIN_STATUS).post(true)
                            ToastUtils.show("登录成功")
                            hideLoading()
                        }
                    }
                }
        }
    }

    fun showLoading() {
        launcher {
            mLoadingDialogFlow.emit(true)
        }
    }

    fun hideLoading() {
        launcher {
            mLoadingDialogFlow.emit(false)
        }
    }
}