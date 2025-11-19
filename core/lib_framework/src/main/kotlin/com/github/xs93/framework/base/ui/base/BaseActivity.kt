package com.github.xs93.framework.base.ui.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.github.xs93.framework.base.ui.interfaces.IActivity
import com.github.xs93.framework.base.ui.interfaces.IWindowInsetsListener
import com.github.xs93.framework.loading.ICreateLoadingDialog
import com.github.xs93.framework.loading.ILoadingDialogControl
import com.github.xs93.framework.loading.ILoadingDialogControlProxy
import com.github.xs93.framework.loading.LoadingDialogHelper
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy


/**
 * 基础activity类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:01
 */
abstract class BaseActivity : AppCompatActivity(), IActivity, IToast by UiToastProxy(),
    ICreateLoadingDialog, ILoadingDialogControl, IWindowInsetsListener {

    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(supportFragmentManager, this, this)
    }

    private val windowInsetsHelper = WindowInsetsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSuperOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setupEnableEdgeToEdge(this)
        beforeSetContentView(savedInstanceState)
        if (contentLayoutId != 0) {
            setContentView(contentLayoutId)
        } else {
            customSetContentView()
        }
        val decorView = window.decorView
        windowInsetsHelper.attach(decorView, this)
        initView(savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    //region EdgeToEdge样式设置
    protected open fun setupEnableEdgeToEdge(activity: ComponentActivity) {
        activity.enableEdgeToEdge(
            SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
    }
    //endregion

    override fun createLoadingDialog(): DialogFragment {
        return LoadingDialogHelper.createLoadingDialog()
    }

    override fun showLoadingDialog() {
        mIUiLoadingDialog.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        mIUiLoadingDialog.hideLoadingDialog()
    }
}