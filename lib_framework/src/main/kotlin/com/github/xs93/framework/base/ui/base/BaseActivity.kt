package com.github.xs93.framework.base.ui.base

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import com.github.xs93.framework.base.ui.interfaces.IBaseActivity
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
abstract class BaseActivity : AppCompatActivity(), IBaseActivity, IToast by UiToastProxy(),
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
        val layoutId = getContentLayoutId()
        if (layoutId != 0) {
            setContentView(layoutId)
        } else {
            val contentView = getContentView()
            if (contentView != null) {
                setContentView(contentView)
            } else {
                customSetContentView()
            }
        }

        val decorView = window.decorView
        windowInsetsHelper.attach(decorView, this)
        val controllerCompat = WindowCompat.getInsetsController(window, decorView)
        controllerCompat.let {
            it.isAppearanceLightStatusBars = isAppearanceLightStatusBars()
            it.isAppearanceLightNavigationBars = isAppearanceLightNavigationBars()
        }

        initView(savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }

    //region EdgeToEdge样式设置
    protected open fun setupEnableEdgeToEdge(activity: ComponentActivity) {
        activity.enableEdgeToEdge(
            SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
    }

    /**
     * 修改导航栏图标是否是浅色
     */
    protected open fun isAppearanceLightNavigationBars(): Boolean {
        val nightMode =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        return !nightMode
    }

    /**
     * 修改状态栏图标是否是浅色
     */
    protected open fun isAppearanceLightStatusBars(): Boolean {
        val nightMode =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        return !nightMode
    }
    //endregion

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onSystemBarInsetsChanged(insets: Insets) {

    }

    override fun onSoftKeyboardHeightChanged(imeVisible: Boolean, height: Int) {

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