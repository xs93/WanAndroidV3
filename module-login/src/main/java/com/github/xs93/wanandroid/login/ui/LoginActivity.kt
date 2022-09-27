package com.github.xs93.wanandroid.login.ui

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.xs93.core.base.ui.vbvm.BaseVbVmActivity
import com.github.xs93.core.ktx.repeatOnLifecycle
import com.github.xs93.wanandroid.common.interfaces.ILoadingDialog
import com.github.xs93.wanandroid.common.interfaces.impl.LoadingDialogImpl
import com.github.xs93.wanandroid.common.router.RouterPath
import com.github.xs93.wanandroid.login.R
import com.github.xs93.wanandroid.login.databinding.ActivityLoginBinding
import com.github.xs93.wanandroid.login.viewmodel.LoginViewModel

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
        binding.activity = this
        binding.etAccount.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                binding.tilAccount.isErrorEnabled = false
            }
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                binding.tilPassword.isErrorEnabled = false
            }
        }

        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loadDialogFlow.collect {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    fun clickBack() {
        finish()
    }

    fun clickLogin(username: String?, password: String?) {
        if (username.isNullOrBlank()) {
            binding.tilAccount.error = getString(R.string.login_error_input_account)
            return
        }
        if (password.isNullOrBlank()) {
            binding.tilPassword.error = getString(R.string.login_error_inout_password)
            return
        }
        viewModel.login(username, password)
    }

    fun clickRegisterAccount() {

    }
}
