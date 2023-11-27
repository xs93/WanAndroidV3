package com.github.xs93.wanandroid.app.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.github.xs93.framework.base.ui.databinding.BaseDataBindingActivity
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.observer
import com.github.xs93.framework.ktx.observerEvent
import com.github.xs93.utils.ktx.string
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.LoginActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 登录界面
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/10/7 15:10
 * @email 466911254@qq.com
 */

@AndroidEntryPoint
class LoginActivity : BaseDataBindingActivity<LoginActivityBinding>(R.layout.login_activity) {


    private val clickHandler = ClickHandler()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {

            with(etAccount) {
                doOnTextChanged { text, _, _, _ ->
                    if (!text.isNullOrBlank()) {
                        loginViewModel.loginAction.sendAction(
                            LoginAction.AccountErrorEnableAction(false)
                        )
                    }
                }
            }

            with(etPassword) {
                doOnTextChanged { text, _, _, _ ->
                    if (!text.isNullOrBlank()) {
                        loginViewModel.loginAction.sendAction(
                            LoginAction.PwdErrorEnableAction(false)
                        )
                    }
                }
            }

            clickHandler = this@LoginActivity.clickHandler
        }
    }


    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        loginViewModel.registerCommonEvent(this)

        observer(loginViewModel.loginStateFlow) {
            binding.loginState = it
        }

        observerEvent(loginViewModel.loginEventFlow) {
            when (it) {
                is LoginEvent.LoginResultEvent -> {
                    if (it.success) {
                        showToast(string(R.string.login_success))
                        finish()
                    } else {
                        showToast(it.errorMsg ?: string(R.string.login_failed))
                    }
                }
            }
        }
    }

    inner class ClickHandler {

        fun clickBack() {
            finish()
        }

        fun clickLogin(username: String?, password: String?) {
            loginViewModel.loginAction.sendAction(LoginAction.ClickLoginAction(username, password))
        }

        fun clickRegisterAccount() {

        }
    }
}