package com.github.xs93.framework.base.ui.base

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy


/**
 * 基础activity类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:01
 */
abstract class BaseActivity : FragmentActivity(), IToast by UiToastProxy() {


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}