package com.github.xs93.framework.base.ui.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.xs93.framework.base.ui.utils.BaseDialogFragmentConfig
import com.github.xs93.framework.ktx.setOnInsertsChangedListener
import com.github.xs93.framework.loading.ICreateLoadingDialog
import com.github.xs93.framework.loading.ILoadingDialogControl
import com.github.xs93.framework.loading.ILoadingDialogControlProxy
import com.github.xs93.framework.loading.LoadingDialogHelper
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy
import com.github.xs93.framework.ui.WindowSurface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.reflect.Field

/**
 * 底部弹出弹窗dialog
 *
 * @author XuShuai
 * @version v1.0
 * @date 2023/6/16 14:17
 * @email 466911254@qq.com
 */
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(),
    IToast by UiToastProxy(),
    ICreateLoadingDialog,
    ILoadingDialogControl {


    protected val windowSurface = WindowSurface()

    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(childFragmentManager, viewLifecycleOwner, this)
    }

    var onDismissListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val styleId = if (getCustomStyle() != 0) {
            getCustomStyle()
        } else {
            BaseDialogFragmentConfig.commonBottomSheetDialogTheme
        }
        if (styleId != 0) {
            setStyle(DialogFragment.STYLE_NORMAL, styleId)
        }
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

        if (showFixedHeight()) {
            view.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (view.measuredHeight == 0) {
                        return
                    }
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val dialog = dialog
                    if (dialog is BottomSheetDialog) {
                        val bottomSheet: FrameLayout =
                            dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) ?: return
                        val behavior: BottomSheetBehavior<FrameLayout> =
                            BottomSheetBehavior.from(bottomSheet)
                        behavior.peekHeight = view.measuredHeight
                    }
                }
            })
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

    protected open fun getCustomStyle(): Int {
        return 0
    }

    /**
     * 直接显示完整的高度,不需要上滑
     * @return Boolean
     */
    open fun showFixedHeight(): Boolean {
        return true
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

    /**
     * 使用此方法显示弹出框，可以避免生命周期状态错误导致的异常(Can not perform this action after onSaveInstanceState)
     * @param manager FragmentManager
     * @param tag String?
     */
    fun showAllowingStateLoss(
        manager: FragmentManager,
        tag: String? = this::class.java.simpleName
    ) {
        try {
            val dismissed: Field = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed.set(this, false)
            val shown: Field = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shown.isAccessible = true
            shown.set(this, true)
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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