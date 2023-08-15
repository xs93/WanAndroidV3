package com.github.xs93.framework.base.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.github.xs93.framework.ktx.setOnInsertsChangedListener
import com.github.xs93.framework.loading.IUiLoadingDialog
import com.github.xs93.framework.loading.IUiLoadingDialogProxy
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy
import com.github.xs93.framework.ui.Surface


/**
 * 基础activity类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:01
 */
abstract class BaseActivity : AppCompatActivity(), IToast by UiToastProxy(), IUiLoadingDialog {

    var resumed: Boolean = false
        private set

    protected val surface = Surface()

    private val mIUiLoadingDialog by lazy {
        IUiLoadingDialogProxy(supportFragmentManager, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSuperOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
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
            surface.insets = it
        }

        beforeInitView(savedInstanceState)
        initView(savedInstanceState)
        initObserver(savedInstanceState)
        beforeInitData(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        resumed = true
    }

    override fun onPause() {
        super.onPause()
        resumed = false
    }

    /**执行在super.onCreate(savedInstanceState)之前*/
    open fun beforeSuperOnCreate(savedInstanceState: Bundle?) {}

    /**在setContentView之前执行*/
    open fun beforeSetContentView(savedInstanceState: Bundle?) {}

    /**返回布局Layout*/
    @LayoutRes
    abstract fun getContentLayoutId(): Int

    /**返回ContentView*/
    abstract fun getContentView(): View?

    /** 自定义如何去调用SetContentView方法，调用此方法前提是 getContentLayoutId ==0 并且 getContentView ==null */
    open fun customSetContentView() {}

    /**在initView方法之前执行*/
    open fun beforeInitView(savedInstanceState: Bundle?) {}

    /**初始化View*/
    abstract fun initView(savedInstanceState: Bundle?)

    /** 初始化订阅观察者 */
    open fun initObserver(savedInstanceState: Bundle?) {}

    /**在 initData 方法之前执行*/
    open fun beforeInitData(savedInstanceState: Bundle?) {}

    /** 初始化数据 */
    open fun initData(savedInstanceState: Bundle?) {}


    override fun createLoadingDialog(): DialogFragment {
        return mIUiLoadingDialog.createLoadingDialog()
    }

    override fun showLoadingDialog(message: CharSequence?) {
        mIUiLoadingDialog.showLoadingDialog(message)
    }

    override fun updateLoadingDialog(message: CharSequence) {
        mIUiLoadingDialog.updateLoadingDialog(message)
    }

    override fun hideLoadingDialog() {
        mIUiLoadingDialog.hideLoadingDialog()
    }
}