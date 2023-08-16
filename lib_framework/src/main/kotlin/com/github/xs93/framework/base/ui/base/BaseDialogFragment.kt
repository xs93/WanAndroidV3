package com.github.xs93.framework.base.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.github.xs93.framework.R
import com.github.xs93.framework.ktx.setOnInsertsChangedListener
import com.github.xs93.framework.loading.ICreateLoadingDialog
import com.github.xs93.framework.loading.ILoadingDialogControl
import com.github.xs93.framework.loading.ILoadingDialogControlProxy
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy
import com.github.xs93.framework.ui.WindowSurface

/**
 * 基础dialogFragment 封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 13:40
 */
abstract class BaseDialogFragment : AppCompatDialogFragment(), IToast by UiToastProxy(),
    ICreateLoadingDialog,
    ILoadingDialogControl {

    protected val windowSurface = WindowSurface()

    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(childFragmentManager, viewLifecycleOwner, this)
    }

    var onDismissListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, getStyle())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DialogInterfaceProxyDialog(requireContext(), theme)
    }

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

        dialog?.window?.apply {
            val contentView: View = decorView.findViewById(android.R.id.content)
            contentView.setOnInsertsChangedListener {
                windowSurface.contentPadding = it
            }
        }
        beforeInitView(view, savedInstanceState)
        initView(view, savedInstanceState)
        initObserver(savedInstanceState)
        beforeInitData(savedInstanceState)
        initData(savedInstanceState)
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

    protected open fun getStyle(): Int {
        return R.style.BaseDialogStyle
    }

    /**返回布局Layout*/
    @LayoutRes
    abstract fun getContentLayoutId(): Int

    open fun beforeInitView(view: View, savedInstanceState: Bundle?) {}

    /** 初始化View */
    abstract fun initView(view: View, savedInstanceState: Bundle?)


    /** 初始化订阅观察者 */
    open fun initObserver(savedInstanceState: Bundle?) {}

    open fun beforeInitData(savedInstanceState: Bundle?) {}

    /** 初始化数据 */
    open fun initData(savedInstanceState: Bundle?) {}

    override fun createLoadingDialog(): DialogFragment {
        return mIUiLoadingDialog.createLoadingDialog()
    }

    override fun showLoadingDialog() {
        mIUiLoadingDialog.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        mIUiLoadingDialog.hideLoadingDialog()
    }
}