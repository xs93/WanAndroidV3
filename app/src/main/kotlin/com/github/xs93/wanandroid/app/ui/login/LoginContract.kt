package com.github.xs93.wanandroid.app.ui.login

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

/**
 * 启动登录Contract
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/11/27 9:28
 * @email 466911254@qq.com
 */
class LoginContract : ActivityResultContract<Unit, Boolean>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, LoginActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        TODO("Not yet implemented")
    }
}