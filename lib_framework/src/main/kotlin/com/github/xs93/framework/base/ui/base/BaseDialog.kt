package com.github.xs93.framework.base.ui.base

import android.content.Context
import android.content.DialogInterface

/**
 * 基础Dialog基类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2022/4/22 17:26
 */
class BaseDialog : DialogInterfaceProxyDialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(
        context,
        cancelable,
        cancelListener
    )
}