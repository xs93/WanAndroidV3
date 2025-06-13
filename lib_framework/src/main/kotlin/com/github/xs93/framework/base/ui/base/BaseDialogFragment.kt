package com.github.xs93.framework.base.ui.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.graphics.Insets
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.xs93.framework.R
import com.github.xs93.framework.base.ui.interfaces.IBaseFragment
import com.github.xs93.framework.base.ui.interfaces.IWindowInsetsListener
import com.github.xs93.framework.loading.ICreateLoadingDialog
import com.github.xs93.framework.loading.ILoadingDialogControl
import com.github.xs93.framework.loading.ILoadingDialogControlProxy
import com.github.xs93.framework.loading.LoadingDialogHelper
import com.github.xs93.framework.toast.IToast
import com.github.xs93.framework.toast.UiToastProxy
import java.lang.reflect.Field

/**
 * 基础dialogFragment 封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 13:40
 */
abstract class BaseDialogFragment : AppCompatDialogFragment(), IBaseFragment,
    IToast by UiToastProxy(),
    ICreateLoadingDialog, ILoadingDialogControl, IWindowInsetsListener {

    private val mIUiLoadingDialog by lazy {
        ILoadingDialogControlProxy(childFragmentManager, viewLifecycleOwner, this)
    }

    private val dismissListeners by lazy { mutableListOf<() -> Unit>() }

    private val windowInsetsHelper = WindowInsetsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val styleId = getCustomStyle()
        if (styleId != 0) {
            setStyle(STYLE_NO_TITLE, styleId)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = customDialog(requireContext(), theme) ?: BaseDialog(requireContext(), theme)
        dialog.window?.let {
            setupEnableEdgeToEdge(it)
            setupWindow(it)
        }
        return dialog
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


    override fun onSystemBarInsetsChanged(insets: Insets) {

    }

    override fun onSoftKeyboardHeightChanged(imeVisible: Boolean, height: Int) {

    }

    //region 样式设置
    /**
     * 自定义Dialog返回,返回null，则默认使用BaseDialog
     */
    protected open fun customDialog(context: Context, theme: Int): Dialog? = null

    /**
     * 自定义Dialog样式,0则使用默认样式
     */
    protected open fun getCustomStyle(): Int {
        return if (isImmersive()) {
            R.style.BaseDialogTheme_Immersive
        } else {
            if (isFullScreen()) {
                R.style.BaseDialogTheme_FullScreen
            } else {
                R.style.BaseDialogTheme
            }
        }
    }

    protected open fun setupEnableEdgeToEdge(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        }
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


    /**
     * 修改window 属性样式
     */
    protected open fun setupWindow(window: Window) {
        window.decorView.setPadding(0, 0, 0, 0)
        window.setLayout(windowWidth(), windowHeight())
        if (isBottomDialog()) {
            window.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            window.setWindowAnimations(R.style.BaseBottomDialogWindowAnim)
        } else {
            window.setGravity(Gravity.CENTER)
        }
    }

    protected open fun windowWidth(): Int {
        return if (isFullScreen() || isImmersive()) {
            WindowManager.LayoutParams.MATCH_PARENT
        } else {
            WindowManager.LayoutParams.WRAP_CONTENT
        }
    }

    protected open fun windowHeight(): Int {
        return if (isFullScreen()) {
            WindowManager.LayoutParams.MATCH_PARENT
        } else {
            WindowManager.LayoutParams.WRAP_CONTENT
        }
    }

    /**
     * 是否是全屏弹窗
     */
    protected open fun isFullScreen(): Boolean = false

    /**
     * 是否是沉浸式弹窗
     */
    protected open fun isImmersive(): Boolean = false

    /**
     * 是否是底部弹窗
     */
    protected open fun isBottomDialog(): Boolean = false


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

    fun addOnDismissListener(listener: () -> Unit) {
        dismissListeners.add(listener)
    }

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