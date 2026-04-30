package com.github.xs93.ui.base.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.WindowCompat
import com.github.xs93.ui.R
import com.github.xs93.ui.base.ui.base.interfaces.IDialogFragment
import com.github.xs93.ui.base.ui.base.interfaces.IWindowInsetsListener

/**
 * 基础dialogFragment 封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/4 13:40
 */
abstract class BaseDialogFragment : AppCompatDialogFragment(), IDialogFragment,
    IWindowInsetsListener {

    private val windowInsetsHelper = WindowInsetsHelper()

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
        windowInsetsHelper.attach(view, this)
        initParams(arguments)
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
        if (isImmersive()) {
            return R.style.BaseDialogTheme_Immersive
        }
        return 0
    }

    /**
     * dismiss监听
     */
    fun addOnDismissListener(listener: () -> Unit) {
        dismissListeners.add(listener)
    }

    //region IDialogFragment
    override fun isImmersive(): Boolean = true

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
    //endregion
}