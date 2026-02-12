package com.github.xs93.ui.base.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.WindowCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import com.github.xs93.core.toast.IToast
import com.github.xs93.core.toast.UiToastProxy
import com.github.xs93.ui.R
import com.github.xs93.ui.base.ui.interfaces.IBottomSheetDialogFragment
import com.github.xs93.ui.base.ui.interfaces.IWindowInsetsListener
import com.github.xs93.ui.loading.ICreateLoadingDialog
import com.github.xs93.ui.loading.ILoadingDialogControl
import com.github.xs93.ui.loading.ILoadingDialogControlProxy
import com.github.xs93.ui.loading.LoadingDialogHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * 底部弹出弹窗dialog
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/6/16 14:17
 * @email 466911254@qq.com
 */
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(),
    IBottomSheetDialogFragment, IToast by UiToastProxy(), ICreateLoadingDialog,
    ILoadingDialogControl, IWindowInsetsListener {

    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(childFragmentManager, viewLifecycleOwner, this)
    }

    private val dismissListeners by lazy { mutableListOf<() -> Unit>() }

    private val windowInsetsHelper = WindowInsetsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val styleId = getCustomStyle()
        if (styleId != 0) {
            setStyle(STYLE_NORMAL, styleId)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window
        window?.let {
            WindowCompat.enableEdgeToEdge(it)
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
        val decorView = dialog?.window?.decorView
        decorView?.let {
            windowInsetsHelper.attach(it, this)
        }
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


    protected open fun getCustomStyle(): Int {
        return R.style.BaseBottomSheetDialogTheme
    }

    fun addOnDismissListener(listener: () -> Unit) {
        dismissListeners.add(listener)
    }

    //region loading 弹窗
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

    //region 定制BottomSheetDialog
    fun getSheetBehavior(): BottomSheetBehavior<*>? {
        val dialog = dialog ?: return null
        if (dialog !is BottomSheetDialog) return null
        return dialog.behavior
    }

    fun withSheetBehavior(block: BottomSheetBehavior<*>.() -> Unit) {
        val behavior = getSheetBehavior()
        behavior?.block()
    }

    /**
     * 内容高度,可以设置最大高度
     * @param maxHeight Int dialog最大高度
     */
    fun warpContentHeight(maxHeight: Int = -1) {
        withSheetBehavior {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            if (maxHeight != -1) {
                this.maxHeight = maxHeight
            }
        }
    }

    /**
     * 设置固定高度
     * @param height Int
     */
    fun setFixedHeight(height: Int) {
        val dialog = dialog ?: return
        if (dialog !is BottomSheetDialog) return
        val bottomSheet: FrameLayout =
            dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) ?: return
        bottomSheet.updateLayoutParams<ViewGroup.LayoutParams> {
            this.height = height
        }
        val behavior = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.peekHeight = height
        behavior.skipCollapsed = true
    }
    //endregion
}