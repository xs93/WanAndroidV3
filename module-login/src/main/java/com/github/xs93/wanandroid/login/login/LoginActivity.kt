package com.github.xs93.wanandroid.login.login

import android.os.Bundle
import android.webkit.CookieManager
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.xs93.core.base.ui.vbvm.BaseVbVmActivity
import com.github.xs93.core.ktx.repeatOnLifecycle
import com.github.xs93.core.utils.toast.ToastUtils
import com.github.xs93.wanandroid.common.interfaces.ILoadingDialog
import com.github.xs93.wanandroid.common.interfaces.impl.LoadingDialogImpl
import com.github.xs93.wanandroid.common.router.RouterPath
import com.github.xs93.wanandroid.login.R
import com.github.xs93.wanandroid.login.databinding.ActivityLoginBinding

/**
 *
 *  登录界面
 *
 * @author xushuai
 * @date   2022/9/7-14:29
 * @email  466911254@qq.com
 */
@Route(path = RouterPath.Login.LoginActivity)
class LoginActivity : BaseVbVmActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login),
    ILoadingDialog by LoadingDialogImpl() {


    override fun initView(savedInstanceState: Bundle?) {
        registerLoadingDialog(supportFragmentManager, this)
        binding.apply {
            listener = Listener()
            etAccount.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrBlank()) {
                    viewModel.input(LoginIntent.AccountErrorEnableIntent(false))
                }
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrBlank()) {
                    viewModel.input(LoginIntent.PwdErrorEnableIntent(false))
                }
            }
        }
    }

    override fun initObserver(savedInstanceState: Bundle?) {
        super.initObserver(savedInstanceState)
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.stateFlow.collect {
                binding.loginState = it
            }
        }

        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.eventFlow.collect {
                when (it) {
                    LoginViewEvent.HideLoadingEvent -> {
                        hideLoading()
                    }
                    LoginViewEvent.ShowLoadingEvent -> {
                        showLoading()
                    }
                    is LoginViewEvent.ToastEvent -> {
                        ToastUtils.show(it.msg)
                    }
                    LoginViewEvent.FinishEvent -> {
                        finish()
                    }
                }
            }
        }

    }

    inner class Listener {
        fun clickBack() {
            finish()
        }

        fun clickLogin(username: String?, password: String?) {
            viewModel.input(LoginIntent.ClickLoginIntent(username, password))
        }

        fun clickRegisterAccount() {
            viewModel.input(LoginIntent.RegisterAccountIntent)
        }
    }
}
