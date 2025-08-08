package com.github.xs93.framework.base.ui.base

import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.WindowCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.xs93.framework.base.ui.interfaces.IBaseFragment
import com.github.xs93.framework.base.ui.interfaces.IWindowInsetsListener
import com.github.xs93.framework.loading.ICreateLoadingDialog
import com.github.xs93.framework.loading.ILoadingDialogControl
import com.github.xs93.framework.loading.ILoadingDialogControlProxy
import com.github.xs93.framework.loading.LoadingDialogHelper
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy
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
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), IBaseFragment,
    IToast by UiToastProxy(), ICreateLoadingDialog, ILoadingDialogControl, IWindowInsetsListener {

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
        val window = dialog?.window
        window?.let {
            val controllerCompat = WindowCompat.getInsetsController(it, it.decorView)
            controllerCompat.isAppearanceLightStatusBars = isAppearanceLightStatusBars()
            controllerCompat.isAppearanceLightNavigationBars = isAppearanceLightNavigationBars()
        }

        initView(view, savedInstanceState)
        initObserver(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val iterator = dismissListeners.iterator()
        while (iterator.hasNext()) {
            iterator.next().invoke()
        }
    }

    protected open fun getCustomStyle(): Int {
        return 0
    }


    //region 样式设置
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

    fun addOnDismissListener(listener: () -> Unit) {
        dismissListeners.add(listener)
    }

    //region 定制BottomSheetDialog
    fun getSheetBehavior(): BottomSheetBehavior<*>? {
        val dialog = dialog ?: return null
        if (dialog !is BottomSheetDialog) return null
        return dialog.behavior
    }

    /**
     * 内容高度,可以设置最大高度
     * @param maxHeight Int dialog最大高度
     */
    fun warpContentHeight(maxHeight: Int = -1) {
        val dialog = dialog ?: return
        if (dialog !is BottomSheetDialog) return
        val behavior = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        if (maxHeight != -1) {
            behavior.maxHeight = maxHeight
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


}