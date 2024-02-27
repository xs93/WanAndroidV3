package com.github.xs93.framework.base.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.github.xs93.framework.base.ui.interfaces.IBaseFragment
import com.github.xs93.framework.base.ui.utils.BaseDialogFragmentConfig
import com.github.xs93.framework.ktx.isStatusBarTranslucentCompat
import com.github.xs93.framework.ktx.setOnInsertsChangedListener
import com.github.xs93.framework.loading.ICreateLoadingDialog
import com.github.xs93.framework.loading.ILoadingDialogControl
import com.github.xs93.framework.loading.ILoadingDialogControlProxy
import com.github.xs93.framework.loading.LoadingDialogHelper
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy

/**
 * 基础dialogFragment 封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 13:40
 */
abstract class BaseDialogFragment : AppCompatDialogFragment(), IBaseFragment, IToast by UiToastProxy(),
    ICreateLoadingDialog, ILoadingDialogControl {

    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(childFragmentManager, viewLifecycleOwner, this)
    }

    var onDismissListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val styleId = if (getCustomStyle() != 0) {
            getCustomStyle()
        } else {
            BaseDialogFragmentConfig.commonDialogTheme
        }
        if (styleId != 0) {
            setStyle(DialogFragment.STYLE_NORMAL, styleId)
        }
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
            isStatusBarTranslucentCompat = true
            val contentView: View = decorView.findViewById(android.R.id.content)
            contentView.setOnInsertsChangedListener {
                onSystemBarInsetsChanged(it)
            }
        }
        initView(view, savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

    protected open fun getCustomStyle(): Int {
        return 0
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