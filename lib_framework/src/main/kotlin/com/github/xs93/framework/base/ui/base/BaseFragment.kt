package com.github.xs93.framework.base.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.github.xs93.framework.base.ui.interfaces.IBaseFragment
import com.github.xs93.framework.base.ui.interfaces.IWindowInsetsListener
import com.github.xs93.framework.loading.ICreateLoadingDialog
import com.github.xs93.framework.loading.ILoadingDialogControl
import com.github.xs93.framework.loading.ILoadingDialogControlProxy
import com.github.xs93.framework.loading.LoadingDialogHelper
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy

/**
 * 基础Fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 11:25
 */
abstract class BaseFragment : Fragment(), IBaseFragment, IToast by UiToastProxy(),
    ICreateLoadingDialog, ILoadingDialogControl, IWindowInsetsListener {


    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(childFragmentManager, viewLifecycleOwner, this)
    }

    private var mFirstVisibleCalled: Boolean = false

    private val windowInsetsHelper = WindowInsetsHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (getContentLayoutId() != 0) {
            return inflater.inflate(getContentLayoutId(), container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        windowInsetsHelper.attach(view, this)

        initView(view, savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("FIRST_VISIBLE_CALLED", mFirstVisibleCalled)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mFirstVisibleCalled = savedInstanceState?.getBoolean("FIRST_VISIBLE_CALLED") == true
    }

    override fun onResume() {
        super.onResume()
        if (!mFirstVisibleCalled) {
            onFirstVisible()
            mFirstVisibleCalled = true
        }
    }

    /** 该fragment 第一次被显示时调用,可用作懒加载 */
    open fun onFirstVisible() {}

    /**
     * onFirstVisible是否被调用过
     * @return Boolean true 被调用过,false没有被调用过
     */
    fun firstVisibleCalled(): Boolean = mFirstVisibleCalled


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