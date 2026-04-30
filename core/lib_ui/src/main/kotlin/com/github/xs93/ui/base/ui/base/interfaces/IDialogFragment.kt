package com.github.xs93.ui.base.ui.base.interfaces

import android.os.Bundle

/**
 * @author XuShuai
 * @version v1.0
 * @date 2025/11/19 10:53
 * @description 弹窗Fragment接口
 *
 */
interface IDialogFragment : IDialog {

    /** 是否沉浸式 */
    fun isImmersive(): Boolean

    /** 初始化参数 */
    fun initParams(arguments: Bundle?) {}
}