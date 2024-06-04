package com.github.xs93.framework.base.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.github.xs93.framework.base.ui.interfaces.IBaseActivity
import com.github.xs93.framework.ktx.setOnInsertsChangedListener
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
abstract class BaseActivity : AppCompatActivity(), IBaseActivity, IToast by UiToastProxy(), ICreateLoadingDialog,
    ILoadingDialogControl {

    var resumed: Boolean = false
        private set

    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(supportFragmentManager, this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSuperOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setupEnableEdgeToEdge(this)
        beforeSetContentView(savedInstanceState)
        if (getContentLayoutId() != 0) {
            setContentView(getContentLayoutId())
        } else if (getContentView() != null) {
            setContentView(getContentView())
        } else {
            customSetContentView()
        }

        val contentView: View? = findViewById(android.R.id.content)
        contentView?.setOnInsertsChangedListener {
            onSystemBarInsetsChanged(it)
        }

        initView(savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }

    open fun setupEnableEdgeToEdge(activity: ComponentActivity) {
        activity.enableEdgeToEdge()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

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