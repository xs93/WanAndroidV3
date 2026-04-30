package com.github.xs93.ui.base.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.core.view.WindowCompat
import androidx.core.view.updateLayoutParams
import com.github.xs93.ui.R
import com.github.xs93.ui.base.ui.base.interfaces.IBottomSheetDialogFragment
import com.github.xs93.ui.base.ui.base.interfaces.IWindowInsetsListener
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
    IBottomSheetDialogFragment, IWindowInsetsListener {

    private val windowInsetsHelper = WindowInsetsHelper()
    private val dismissListeners by lazy { mutableListOf<() -> Unit>() }

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
            initWindowStyle(window)
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
        val attachView = dialog?.window?.decorView ?: view
        windowInsetsHelper.attach(attachView, this)
        initParams(arguments)
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
        return R.style.BaseBottomSheetDialogTheme
    }

    fun addOnDismissListener(listener: () -> Unit) {
        dismissListeners.add(listener)
    }

    //region IBottomSheetDialogFragment
    final override fun isImmersive(): Boolean = true

    override fun initWindowStyle(window: Window) {
        WindowCompat.enableEdgeToEdge(window)
    }

    override fun setWindowSize(width: Int, height: Int) {
        val window = dialog?.window
        window?.let {
            val layoutParams = it.attributes
            layoutParams.width = width
            layoutParams.height = height
            it.attributes = layoutParams
        }
    }

    override fun setFullScreenSize() {
        setWindowSize(-1, -1)
    }

    override fun setBottomDialog(isBottomDialog: Boolean) {
        val window = dialog?.window
        window?.run {
            if (isBottomDialog) {
                setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
                setWindowAnimations(R.style.BaseBottomDialogWindowAnim)
            } else {
                setGravity(Gravity.CENTER)
            }
        }
    }

    override fun getSheetBehavior(): BottomSheetBehavior<*>? {
        val dialog = dialog ?: return null
        if (dialog !is BottomSheetDialog) return null
        return dialog.behavior
    }

    override fun withSheetBehavior(block: BottomSheetBehavior<*>.() -> Unit) {
        val behavior = getSheetBehavior()
        behavior?.block()
    }

    /**
     * 内容高度,可以设置最大高度
     * @param maxHeight Int dialog最大高度
     */
    override fun warpContentHeight(maxHeight: Int) {
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
    override fun setFixedHeight(height: Int) {
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