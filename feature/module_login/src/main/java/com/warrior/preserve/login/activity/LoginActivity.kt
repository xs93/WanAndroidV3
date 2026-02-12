package com.warrior.preserve.login.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import com.github.xs93.core.ktx.observerEvent
import com.github.xs93.core.ktx.observerState
import com.github.xs93.core.ktx.setSingleClickListener
import com.github.xs93.core.ktx.string
import com.github.xs93.ui.base.ui.viewbinding.BaseVBActivity
import com.github.xs93.ui.base.viewmodel.registerCommonEvent
import com.github.xs93.wan.common.viewmodel.LoginAction
import com.github.xs93.wan.common.viewmodel.LoginEvent
import com.github.xs93.wan.common.viewmodel.LoginViewModel
import com.github.xs93.wan.login.R
import com.github.xs93.wan.login.databinding.LoginActivityBinding
import com.github.xs93.wan.router.PageRouterPath
import com.therouter.router.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author XuShuai
 * @version v1.0
 * @date 2026/2/2
 * @description 登录界面
 *
 */
@AndroidEntryPoint
@Route(path = PageRouterPath.ACTIVITY_LOGIN)
class LoginActivity : BaseVBActivity<LoginActivityBinding>(LoginActivityBinding::inflate) {


    private val loginViewModel: LoginViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        vBinding.apply {
            with(toolbar) {
                setNavigationOnClickListener {
                    finish()
                }
            }

            with(etAccount) {
                doOnTextChanged { text, _, _, _ ->
                    if (!text.isNullOrBlank()) {
                        loginViewModel.loginAction.send(
                            LoginAction.AccountErrorEnableAction(false)
                        )
                    }
                }
            }

            with(etPassword) {
                doOnTextChanged { text, _, _, _ ->
                    if (!text.isNullOrBlank()) {
                        loginViewModel.loginAction.send(
                            LoginAction.PwdErrorEnableAction(false)
                        )
                    }
                }
            }

            with(btnLogin) {
                setSingleClickListener {
                    val username = etAccount.text?.toString()?.trim()
                    val password = etPassword.text?.toString()?.trim()
                    loginViewModel.loginAction.send(
                        LoginAction.ClickLoginAction(
                            username,
                            password
                        )
                    )
                }
            }
        }
    }


    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        loginViewModel.registerCommonEvent(this)

        observerState(loginViewModel.loginStateFlow) {

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

    override fun onSystemBarInsetsChanged(insets: Insets) {
        super.onSystemBarInsetsChanged(insets)
        vBinding.toolbar.updatePadding(top = insets.top)
    }
}