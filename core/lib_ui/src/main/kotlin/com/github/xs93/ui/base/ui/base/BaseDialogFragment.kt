package com.github.xs93.ui.base.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import com.github.xs93.core.toast.IToast
import com.github.xs93.core.toast.UiToastProxy
import com.github.xs93.ui.R
import com.github.xs93.ui.base.ui.interfaces.IDialogFragment
import com.github.xs93.ui.base.ui.interfaces.IWindowInsetsListener
import com.github.xs93.ui.loading.ICreateLoadingDialog
import com.github.xs93.ui.loading.ILoadingDialogControl
import com.github.xs93.ui.loading.ILoadingDialogControlProxy
import com.github.xs93.ui.loading.LoadingDialogHelper

/**
 * 基础dialogFragment 封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 13:40
 */
abstract class BaseDialogFragment : AppCompatDialogFragment(), IDialogFragment,
    IToast by UiToastProxy(), ICreateLoadingDialog, ILoadingDialogControl, IWindowInsetsListener {

    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(childFragmentManager, viewLifecycleOwner, this)
    }

    private val windowInsetsHelper = WindowInsetsHelper()

    private var isBottomDialog: Boolean = false
    private var windowWidth: Int = -2
    private var windowHeight: Int = -2

    private val dismissListeners by lazy { mutableListOf<() -> Unit>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val styleId = getCustomStyle()
        if (styleId != 0) {
            setStyle(STYLE_NO_TITLE, styleId)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window
        window?.let {
            WindowCompat.enableEdgeToEdge(it)
            setBottomDialog(isBottomDialog)
            setWindowWidth(windowWidth)
            setWindowHeight(windowHeight)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (contentLayoutId != 0) {
            return inflater.inflate(contentLayoutId, container, false)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        windowInsetsHelper.attach(view, this)
        initView(view, savedInstanceState)
        initData(savedInstanceState)
        initObserver(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val iterator = dismissListeners.iterator()
        while (iterator.hasNext()) {
            iterator.next().invoke()
        }
    }

    /**
     * 自定义Dialog样式,0则使用默认样式
     */
    protected open fun getCustomStyle(): Int {
        return if (isImmersive()) {
            R.style.BaseDialogTheme_Immersive
        } else {
            R.style.BaseDialogTheme
        }
    }

    /**
     * dismiss监听
     */
    fun addOnDismissListener(listener: () -> Unit) {
        dismissListeners.add(listener)
    }

    //region IDialogFragment
    override fun isImmersive(): Boolean = false


    override fun setFullScreen() {
        windowWidth = WindowManager.LayoutParams.MATCH_PARENT
        windowHeight = WindowManager.LayoutParams.MATCH_PARENT
        val window = dialog?.window ?: return
        val layoutParams = window.attributes
        layoutParams.width = windowWidth
        layoutParams.height = windowHeight
        window.attributes = layoutParams
    }

    override fun setBottomDialog(isBottomDialog: Boolean) {
        this.isBottomDialog = isBottomDialog
        val window = dialog?.window ?: return
        if (isBottomDialog) {
            window.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            window.setWindowAnimations(R.style.BaseBottomDialogWindowAnim)
        } else {
            window.setGravity(Gravity.CENTER)
        }
    }

    override fun isBottomDialog(): Boolean {
        return isBottomDialog
    }

    override fun setWindowWidth(width: Int) {
        windowWidth = width
        val window = dialog?.window ?: return
        val layoutParams = window.attributes
        layoutParams.width = windowWidth
        window.attributes = layoutParams
    }

    override fun setWindowHeight(height: Int) {
        windowHeight = height
        val window = dialog?.window ?: return
        val layoutParams = window.attributes
        layoutParams.height = windowHeight
        window.attributes = layoutParams
    }
    //endregion


    //region 加载弹窗
    override fun createLoadingDialog(): DialogFragment {
        return LoadingDialogHelper.createLoadingDialog()
    }

    override fun showLoadingDialog() {
        mIUiLoadingDialog.showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        mIUiLoadingDialog.hideLoadingDialog()
    }
    //endregion


}