package com.github.xs93.wanandroid.app.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import com.github.xs93.framework.base.ui.viewbinding.BaseViewBindingActivity
import com.github.xs93.framework.base.viewmodel.registerCommonEvent
import com.github.xs93.framework.ktx.observerEvent
import com.github.xs93.framework.ktx.observerState
import com.github.xs93.utils.ktx.setSingleClickListener
import com.github.xs93.utils.ktx.string
import com.github.xs93.wanandroid.app.R
import com.github.xs93.wanandroid.app.databinding.LoginActivityBinding
import com.github.xs93.wanandroid.app.ui.viewmodel.LoginAction
import com.github.xs93.wanandroid.app.ui.viewmodel.LoginEvent
import com.github.xs93.wanandroid.app.ui.viewmodel.LoginViewModel
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
class LoginActivity :
    BaseViewBindingActivity<LoginActivityBinding>(
        R.layout.login_activity,
        LoginActivityBinding::bind
    ) {


    private val loginViewModel: LoginViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.apply {
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
        binding.toolbar.updatePadding(top = insets.top)
    }
}